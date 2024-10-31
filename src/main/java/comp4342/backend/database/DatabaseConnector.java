package comp4342.backend.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {
    private Connection connection;

    // 尝试连接数据库，返回成功与否
    public boolean connectDB() throws ClassNotFoundException {
        // 1. 加载驱动
        Class.forName("com.mysql.cj.jdbc.Driver");

        // 2. 数据库连接信息
        String url = "jdbc:mysql://192.168.31.188:3306/chat_app?useUnicode=true&characterEncoding=utf-8";
        String username = "root";
        String password = "SecretDB";

        // 3. 连接数据库
        try {
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("数据库连接成功");
            return true;
        } catch (SQLException error) {
            System.err.println("数据库连接失败：" + error.getMessage());
            return false;
        }
    }

    // 断开数据库连接
    public void disconnectDB() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
            System.out.println("数据库连接已关闭");
        }
    }

    // 获取当前连接
    public Connection getConnection() {
        return connection;
    }
}
