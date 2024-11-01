package comp4342.backend.database;

import org.json.JSONObject;

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

    public JSONObject checkUserInfo(int user_id) {
        String sql = "SELECT * FROM user WHERE uid = ?";
        JSONObject userJson = new JSONObject();  // 创建一个 JSONObject 用于存放返回的数据
        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, user_id);
            ResultSet resultSet = stmt.executeQuery();  // 执行查询

            if (resultSet.next()) {  // 判断是否有结果
                userJson.put("uid", resultSet.getInt("uid"));
                userJson.put("uname", resultSet.getString("uname"));
                userJson.put("email", resultSet.getString("email"));
                // 添加其他字段到 JSON 中
                return userJson;  // 成功返回数据
            } else {
                return null;  // 如果没有数据则返回 null
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

//Update Sample
//    public boolean changeName(int userId, String newName) {
//        String sql = "UPDATE user SET uname = ? WHERE uid = ?";
//        try (Connection connection = dbConnector.getConnection();
//             PreparedStatement stmt = connection.prepareStatement(sql)) {
//            stmt.setString(1, newName);
//            stmt.setInt(2, userId);
//            stmt.executeUpdate();
//            return true;
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
   //Query Sample

//    public ResultSet checkFriendList(int userId) {
//        String sql = "SELECT friend_id FROM friends WHERE user_id = ?";
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
