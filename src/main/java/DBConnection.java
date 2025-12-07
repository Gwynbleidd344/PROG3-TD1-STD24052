import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private final String JDBC_URL = "jdbc:postgresql://localhost:5432/product_management_db";
    private final String USER = "postgres";
    private final String PASSWORD = "2048";

    public Connection getDBConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
            System.out.println("Connection to PostgreSQL established successfully.");
        } catch (SQLException e) {
            System.err.println("Failed to establish connection to PostgreSQL.");
            e.printStackTrace();
        }
        return connection;
    }
}