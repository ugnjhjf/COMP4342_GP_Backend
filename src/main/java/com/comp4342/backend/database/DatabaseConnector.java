package com.comp4342.backend.database;

import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Component
public class DatabaseConnector {

    private Connection connection;

    public DatabaseConnector() {
        connectDB();  // 尝试连接数据库
    }

    // 获取当前数据库连接
    public Connection getConnection() {
        // 在每次获取连接时检测其状态，如果失效则重新连接
        if (!isConnectionAlive()) {
            reconnect();
        }
        return connection;
    }

    // 连接到数据库
    private void connectDB() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://www.gnetwork.space/chat_app?useUnicode=true&characterEncoding=utf-8&autoReconnect=true&connectTimeout=5000&socketTimeout=5000";
            String username = "root";
            String password = "SecretDB";
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Database connection successful");
        } catch (ClassNotFoundException e) {
            System.err.println("Database driver not found: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Database connection failure：" + e.getMessage());
        }
    }

    // 检查连接是否有效
    public boolean isConnectionAlive() {
        try {
            return connection != null && !connection.isClosed() && connection.isValid(2);  // 2秒内无响应即视为无效
        } catch (SQLException e) {
            return false;
        }
    }

    // 重连数据库
    public void reconnect() {
        System.out.println("Reconnecting to database...");
        disconnectDB();  // 先关闭已有连接
        connectDB();     // 再次尝试连接
    }

    // 断开数据库连接
    public void disconnectDB() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Database connection closed");
            } catch (SQLException e) {
                System.err.println("Failed to close database connection: " + e.getMessage());
            }
        }
    }
}