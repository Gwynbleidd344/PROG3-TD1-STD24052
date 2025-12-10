import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

class DataRetrieverTest {

    private DataRetriever dataRetriever;
    private List<Category> categories;
    private List<Product> products;

    private final Comparator<Product> productComparator = Comparator.comparing(Product::getId).thenComparing(p -> p.getCategory().getId());
    private final Comparator<Category> categoryComparator = Comparator.comparing(Category::getId);

    @BeforeEach
    void setUp() {
        dataRetriever = new DataRetriever();

        categories = new ArrayList<>();
        categories.add(new Category(1, "Informatique"));
        categories.add(new Category(2, "T,lephone"));
        categories.add(new Category(3, "Audio"));
        categories.add(new Category(4, "Accessoires"));
        categories.add(new Category(5, "Informatique"));
        categories.add(new Category(6, "Bureau"));
        categories.add(new Category(7, "Mobile"));

        products = new ArrayList<>();
        products.add(new Product(1, "Laptop Dell XPS", Instant.parse("2024-01-15T06:30:00Z"), categories.get(0)));
        products.add(new Product(2, "iPhone 13", Instant.parse("2024-02-01T11:10:00Z"), categories.get(1)));
        products.add(new Product(2, "iPhone 13", Instant.parse("2024-02-01T11:10:00Z"), categories.get(6)));
        products.add(new Product(3, "Casque Sony WH1000", Instant.parse("2024-02-10T13:45:00Z"), categories.get(2)));
        products.add(new Product(4, "Clavier Logitech", Instant.parse("2024-03-05T08:20:00Z"), categories.get(3)));
        products.add(new Product(5, "Ecran Samsung 27", Instant.parse("2024-03-18T05:00:00Z"), categories.get(4)));
        products.add(new Product(5, "Ecran Samsung 27", Instant.parse("2024-03-18T05:00:00Z"), categories.get(5)));
    }

    @Test
    @DisplayName("All Categories Test")
    void getAllCategories() throws SQLException {
        List<Category> expected = categories.stream().sorted(categoryComparator).collect(Collectors.toList());
        List<Category> actual = dataRetriever.getAllCategories().stream().sorted(categoryComparator).collect(Collectors.toList());
        assertIterableEquals(expected, actual);
    }

    @Test
    @DisplayName("Product List With Pagination")
    void getProductList() throws SQLException {
        assertAll(
                () -> {
                    List<Product> expected = products.stream().sorted(productComparator).collect(Collectors.toList());
                    List<Product> actual = dataRetriever.getProductList(1, 10).stream().sorted(productComparator).collect(Collectors.toList());
                    assertIterableEquals(expected, actual);
                },
                () ->  {
                    List<Product> expected = products.subList(0, 5).stream().sorted(productComparator).collect(Collectors.toList());
                    List<Product> actual = dataRetriever.getProductList(1,5).stream().sorted(productComparator).collect(Collectors.toList());
                    assertIterableEquals(expected, actual);
                },
                () -> {
                    List<Product> expected = products.subList(0, 3).stream().sorted(productComparator).collect(Collectors.toList());
                    List<Product> actual = dataRetriever.getProductList(1,3).stream().sorted(productComparator).collect(Collectors.toList());
                    assertIterableEquals(expected, actual);
                },
                () -> {
                    List<Product> expected = products.subList(2, 4).stream().sorted(productComparator).collect(Collectors.toList());
                    List<Product> actual = dataRetriever.getProductList(2,2).stream().sorted(productComparator).collect(Collectors.toList());
                    assertIterableEquals(expected, actual);
                }
        );
    }

    @Test
    @DisplayName("Product List By Criteria")
    void getProductsByCriteria() throws SQLException {
        assertAll(
                () -> {
                    List<Product> expected = List.of(products.getFirst()).stream().sorted(productComparator).collect(Collectors.toList());
                    List<Product> actual = dataRetriever.getProductsByCriteria("Dell",null, null, null).stream().sorted(productComparator).collect(Collectors.toList());
                    assertIterableEquals(expected, actual);
                },
                () ->  {
                    List<Product> expected = List.of(products.get(0), products.get(5)).stream().sorted(productComparator).collect(Collectors.toList());
                    List<Product> actual = dataRetriever.getProductsByCriteria(null,"info", null, null).stream().sorted(productComparator).collect(Collectors.toList());
                    assertIterableEquals(expected, actual);
                },
                () -> {
                    List<Product> expected = List.of(products.get(2)).stream().sorted(productComparator).collect(Collectors.toList());
                    List<Product> actual = dataRetriever.getProductsByCriteria("iPhone","mobile", null, null).stream().sorted(productComparator).collect(Collectors.toList());
                    assertIterableEquals(expected, actual);
                },
                () -> {
                    List<Product> expected = List.of(products.get(1), products.get(2), products.get(3)).stream().sorted(productComparator).collect(Collectors.toList());
                    List<Product> actual = dataRetriever.getProductsByCriteria(
                        null,
                        null,
                        Instant.parse("2024-02-01T00:00:00.00Z"),
                        Instant.parse("2024-03-01T00:00:00.00Z")).stream().sorted(productComparator).collect(Collectors.toList());
                    assertIterableEquals(expected, actual);
                },
                () -> {
                    List<Product> expected = List.of(products.get(6)).stream().sorted(productComparator).collect(Collectors.toList());
                    List<Product> actual = dataRetriever.getProductsByCriteria("Samsung", "bureau", null, null).stream().sorted(productComparator).collect(Collectors.toList());
                    assertIterableEquals(expected, actual);
                },
                () -> {
                    List<Product> expected = new ArrayList<>();
                    List<Product> actual = dataRetriever.getProductsByCriteria("Sony", "informatique", null, null);
                    assertIterableEquals(expected, actual);
                },
                () -> {
                    List<Product> expected = List.of(products.get(3)).stream().sorted(productComparator).collect(Collectors.toList());
                    List<Product> actual = dataRetriever.getProductsByCriteria(
                        null,
                        "audio",
                        Instant.parse("2024-01-01T00:00:00.00Z"),
                        Instant.parse("2024-12-01T00:00:00.00Z")).stream().sorted(productComparator).collect(Collectors.toList());
                    assertIterableEquals(expected, actual);
                },
                () -> {
                    List<Product> expected = products.stream().sorted(productComparator).collect(Collectors.toList());
                    List<Product> actual = dataRetriever.getProductsByCriteria(null, null, null, null).stream().sorted(productComparator).collect(Collectors.toList());
                    assertIterableEquals(expected, actual);
                }
        );
    }

    @Test
    @DisplayName("Product List By Criteria With Pagination")
    void getProductsByCriteriaWithPagination() throws SQLException {
        assertAll(
                () -> {
                    List<Product> expected = products.stream().sorted(productComparator).collect(Collectors.toList());
                    List<Product> actual = dataRetriever.getProductsByCriteria(null, null, null, null, 1, 10).stream().sorted(productComparator).collect(Collectors.toList());
                    assertIterableEquals(expected, actual);
                },
                () -> {
                    List<Product> expected = List.of(products.getFirst()).stream().sorted(productComparator).collect(Collectors.toList());
                    List<Product> actual = dataRetriever.getProductsByCriteria("Dell", null, null, null, 1, 5).stream().sorted(productComparator).collect(Collectors.toList());
                    assertIterableEquals(expected, actual);
                },
                () -> {
                    List<Product> expected = List.of(products.get(0), products.get(5)).stream().sorted(productComparator).collect(Collectors.toList());
                    List<Product> actual = dataRetriever.getProductsByCriteria(null, "informatique", null, null, 1, 10).stream().sorted(productComparator).collect(Collectors.toList());
                    assertIterableEquals(expected, actual);
                }
        );
    }
}