package comp4342.backend.database;

import java.sql.*;

public class DatabaseOperator {
    private DatabaseConnector databaseConnector;
    private ResultSet resultSet;

    //Establish connection to the database
    public DatabaseOperator() throws ClassNotFoundException {
        this.databaseConnector = new DatabaseConnector();
        System.out.println("(Database Operator)Database connected: " + databaseConnector.getConnection());
    }
    //Get result set
    public ResultSet getResultSet() {
        return resultSet;
    }

    public boolean checkUserInfo(int uid) {
        String sql = "SELECT * FROM user WHERE uid = ?";
        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, uid);  // 为占位符设置参数
            resultSet = stmt.executeQuery();  // 直接执行查询，无需传入 SQL 字符串

            return resultSet.next();  // 检查是否有结果返回，若有则返回 true
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean changeName(int userId, String newName) {
        String sql = "UPDATE user SET uname = ? WHERE uid = ?";
        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, newName);
            stmt.setInt(2, userId);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
   //Query Sample

//    public ResultSet checkFriendList(int userId) {
//        String sql = "SELECT friend_id FROM friends WHERE uid = ?";
//        try {
//            Connection connection = dbConnector.getConnection();
//            PreparedStatement stmt = connection.prepareStatement(sql);
//            stmt.setInt(1, userId);
//            return stmt.executeQuery();
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
}
