import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class DataRetriever {
    DBConnection DBConnection = new DBConnection();
    Connection connection = DBConnection.getDBConnection();

    public List<Category> getAllCategories() throws SQLException {
        List<Category> categories = new ArrayList<>();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM Product_Category");
        while (resultSet.next()) {
            categories.add(new Category(
                    resultSet.getInt("id"),
                    resultSet.getString("name")
            ));
        }
        resultSet.close();
        statement.close();
        return categories;
    }

    public List<Product> getProductList (int page, int size) throws SQLException {
        List<Product> products = new ArrayList<>();
        int offset = (page - 1) * size;
        String query = " SELECT p.id AS product_id, p.name AS product_name, p.creation_datetime, c.id AS category_id, c.name AS category_name FROM Product p LEFT JOIN Product_Category c ON c.product_id = p.id ORDER BY p.id LIMIT ? OFFSET ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, size);
        preparedStatement.setInt(2, offset);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            Category category = new Category(resultSet.getInt("category_id"), resultSet.getString("category_name"));
            Product product = new Product(
                    resultSet.getInt("product_id"),
                    resultSet.getString("product_name"),
                    resultSet.getTimestamp("creation_datetime").toInstant(),
                    category
            );
            products.add(product);
        }
        resultSet.close();
        preparedStatement.close();
        return products;
    }

    List<Product> getProductsByCriteria(String productName, String categoryName, Instant creationMin, Instant creationMax) throws SQLException {
        List<Product> products = new ArrayList<>();
        StringBuilder query = new StringBuilder("SELECT p.id AS product_id, p.name AS product_name, p.creation_datetime, c.id AS category_id, c.name AS category_name FROM Product p LEFT JOIN Product_Category c ON c.product_id = p.id WHERE 1=1");
        List<Object> params = new ArrayList<>();
        if (productName != null) {
            query.append(" AND p.name ILIKE ?");
            params.add("%" + productName + "%");
        }
        if (categoryName != null) {
            query.append(" AND c.name ILIKE ?");
            params.add("%" + categoryName + "%");
        }
        if (creationMin != null) {
            query.append(" AND p.creation_datetime >= ?");
            params.add(Timestamp.from(creationMin));
        }
        if (creationMax != null) {
            query.append(" AND p.creation_datetime <= ?");
            params.add(Timestamp.from(creationMax));
        }
        PreparedStatement preparedStatement = connection.prepareStatement(query.toString());
        for (int i = 0; i < params.size(); i++) {
            preparedStatement.setObject(i + 1, params.get(i));
        }
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            Category category = new Category(resultSet.getInt("category_id"), resultSet.getString("category_name"));
            Product product = new Product(
                    resultSet.getInt("product_id"),
                    resultSet.getString("product_name"),
                    resultSet.getTimestamp("creation_datetime").toInstant(),
                    category
            );
            products.add(product);
        }
        resultSet.close();
        preparedStatement.close();
        return products;
    }
    List<Product> getProductsByCriteria(String productName, String categoryName, Instant creationMin, Instant creationMax, int page, int size) throws SQLException {
        List<Product> allProducts = getProductsByCriteria(productName, categoryName, creationMin, creationMax);
        int fromIndex = (page - 1) * size;
        if (allProducts.size() < fromIndex) {
            return new ArrayList<>();
        }
        int toIndex = Math.min(fromIndex + size, allProducts.size());
        return allProducts.subList(fromIndex, toIndex);
    }
}
