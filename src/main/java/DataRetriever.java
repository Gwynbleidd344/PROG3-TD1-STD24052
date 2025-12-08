import java.sql.*;
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
        String query = " SELECT p.id AS product_id, p.name AS product_name, p.creation_datetime, c.id AS category_id, c.name AS category_name FROM Product p LEFT JOIN Product_Category c ON c.product_id = p.id ORDER BY p.id LIMIT ? OFFSET ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, page);
        preparedStatement.setInt(2, size);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            Category category = new Category(resultSet.getInt("product_id"), resultSet.getString("product_name"));
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
}
