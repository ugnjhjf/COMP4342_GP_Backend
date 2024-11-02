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
        try {
            stmt = databaseConnector.getConnection().prepareStatement(sql);
            stmt.setInt(1, uid);
            ResultSet resultSet = stmt.executeQuery();  // 执行查询

            if (resultSet.next()) {  // 判断是否有结果
                resultJson.put("uid", resultSet.getInt("uid"));
                resultJson.put("uname", resultSet.getString("uname"));
                resultJson.put("email", resultSet.getString("email"));
                // 添加其他字段到 JSON 中
                return resultJson;  // 成功返回数据
            } else {
                return null;
            }
        }
            catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    public JSONObject changeName(int uid,String newName) throws SQLException {
        sql = "UPDATE user SET uname = ? WHERE uid = ?;";
        try {
            stmt = databaseConnector.getConnection().prepareStatement(sql);
            stmt.setString(1, newName);
            stmt.setInt(2, uid);
            stmt.executeUpdate();  // 执行更新
            return resultJson;  // 成功返回数据

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
