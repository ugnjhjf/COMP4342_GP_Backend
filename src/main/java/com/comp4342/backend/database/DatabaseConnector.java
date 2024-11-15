package comp4342.backend.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {
    private Connection connection;

    public DatabaseConnector() throws ClassNotFoundException {
        connectDB();
    }

    public Connection getConnection() {
        return connection;
    }

    public boolean connectDB() throws ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");

        String url = "jdbc:mysql://www.gnetwork.space/chat_app?useUnicode=true&characterEncoding=utf-8&connectTimeout=10000&socketTimeout=10000";
        String username = "root";
        String password = "SecretDB";
        try {
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Database connection successful");
            return true;
        } catch (SQLException error) {
            System.err.println("Database connection failure：" + error.getMessage());
            return false;
        }
    }

    public void disconnectDB() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
            System.out.println("Database connection closed");
        }
    }
}
