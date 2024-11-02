package comp4342.backend.database;

import org.json.JSONObject;

import java.sql.*;

public class DatabaseOperator {
    private DatabaseConnector databaseConnector;
    //未加参数的SQL
    private Connection connection;
    private String sql;
    //最终查询的SQL
    private PreparedStatement stmt;
    //查询结果
    private ResultSet resultSet;
    //传输到前端的JSON
    private JSONObject resultJson = new JSONObject();

    //Establish connection to the database
    public DatabaseOperator() throws ClassNotFoundException {
        this.databaseConnector = new DatabaseConnector();
        this.connection = databaseConnector.getConnection();
        System.out.println("(Database Operator)Database connected: " + databaseConnector.getConnection());
    }
    //Get result set
    public ResultSet getResultSet() {
        return resultSet;
    }



    public JSONObject checkUserInfo(int uid) throws SQLException {
        sql = "select * from user where uid = ?;";
        try{
            stmt = databaseConnector.getConnection().prepareStatement(sql);
            stmt.setInt(1, uid);
        }catch (SQLException e){
            e.printStackTrace();
        }

        try{
            ResultSet resultSet = stmt.executeQuery();  // 执行查询

            if (resultSet.next()) {  // 判断是否有结果
                resultJson.put("uid", resultSet.getInt("uid"));
                resultJson.put("uname", resultSet.getString("uname"));
                resultJson.put("email", resultSet.getString("email"));
                // 添加其他字段到 JSON 中
                return resultJson;  // 成功返回数据
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
