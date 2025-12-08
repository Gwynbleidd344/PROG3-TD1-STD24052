import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private final String JDBC_URL = "jdbc:postgresql://localhost:5432/product_management_db";
    private final String USER = "postgres";
    private final String PASSWORD = "2048";

    public Connection getDBConnection() {
        try {
            connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
            System.out.println("Connected to PostgreSQL");
            return connection;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}