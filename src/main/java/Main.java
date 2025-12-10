import java.sql.SQLException;
import java.time.Instant;

public class Main {
    public static void main(String[] args) throws SQLException {
        DataRetriever dataRetriever = new DataRetriever();

        dataRetriever.getAllCategories().forEach(System.out::println);

        System.out.println("------------------------------");

        dataRetriever.getProductList(1,10).forEach(System.out::println);
        System.out.println("------------------------------");
        dataRetriever.getProductList(1,5).forEach(System.out::println);
        System.out.println("------------------------------");
        dataRetriever.getProductList(1,3).forEach(System.out::println);
        System.out.println("------------------------------");
        dataRetriever.getProductList(2,2).forEach(System.out::println);

        System.out.println("------------------------------");

        dataRetriever.getProductsByCriteria("Dell", null, null, null).forEach(System.out::println);
        System.out.println("------------------------------");
        dataRetriever.getProductsByCriteria(null, "info", null, null).forEach(System.out::println);
        System.out.println("------------------------------");
        dataRetriever.getProductsByCriteria("iPhone", "mobile", null, null).forEach(System.out::println);
        System.out.println("------------------------------");
        dataRetriever.getProductsByCriteria(null, null, Instant.parse("2024-02-01T00:00:00Z"), Instant.parse("2024-03-01T00:00:00Z")).forEach(System.out::println);
        System.out.println("------------------------------");
        dataRetriever.getProductsByCriteria("Samsung", "bureau", null, null).forEach(System.out::println);
        System.out.println("------------------------------");
        dataRetriever.getProductsByCriteria("Sony", "informatique", null, null).forEach(System.out::println);
        System.out.println("------------------------------");
        dataRetriever.getProductsByCriteria(null, "audio", Instant.parse("2024-01-01T00:00:00Z"), Instant.parse("2024-12-01T00:00:00Z")).forEach(System.out::println);
        System.out.println("------------------------------");
        dataRetriever.getProductsByCriteria(null, null, null, null).forEach(System.out::println);

        System.out.println("------------------------------");

        dataRetriever.getProductsByCriteria(null, null, null, null,1,10).forEach(System.out::println);
        System.out.println("------------------------------");
        dataRetriever.getProductsByCriteria("Dell", null, null, null,1,5).forEach(System.out::println);
        System.out.println("------------------------------");
        dataRetriever.getProductsByCriteria(null, "informatique", null, null,1,10).forEach(System.out::println);
    }
}
