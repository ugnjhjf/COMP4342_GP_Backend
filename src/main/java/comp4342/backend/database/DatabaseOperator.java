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

    public JSONObject checkUserInfo(String uid) throws SQLException {
        sql = "select * from user where uid = ?;";
        try {
            stmt = databaseConnector.getConnection().prepareStatement(sql);
            stmt.setString(1, uid);
            resultSet = stmt.executeQuery();  // 执行查询

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
    public boolean changeName(int uid,String newName)   {
        sql = "UPDATE user SET uname = ? WHERE uid = ?;";
        try {
            stmt = databaseConnector.getConnection().prepareStatement(sql);
            stmt.setString(1, newName);
            stmt.setInt(2, uid);
            stmt.executeUpdate();  // 执行更新return resultJson;  // 成功返回数据
            System.out.println("Change result: "+resultJson.toString());
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public String generateUID() {
        return java.util.UUID.randomUUID().toString();
    }
    public boolean insertRegister(String uname, String email, String password) {
        sql = "INSERT INTO user (uid, uname, email, password) VALUES (?, ?, ?, ?);";
        try {
            stmt = databaseConnector.getConnection().prepareStatement(sql);
            stmt.setString(1, generateUID());
            stmt.setString(2, uname);
            stmt.setString(3, email);
            stmt.setString(4, password);
            System.out.println("Insert result: " + stmt.toString());
            stmt.executeUpdate();  // 执行更新
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public JSONObject login(String email,String password){
        sql = "SELECT uid,uname FROM user where email = ? AND password = ?;";
        try{
            stmt = databaseConnector.getConnection().prepareStatement(sql);
            stmt.setString(1,email);
            stmt.setString(2,password);
            resultSet = stmt.executeQuery();  // 执行查询
            if (resultSet.next()) {  // 判断是否有结果
                resultJson.put("uid", resultSet.getString("uid"));
                resultJson.put("uname",resultSet.getString("uname"));
                resultJson.put("isLogonSucessful",true);
                return resultJson;
            } else {
                resultJson.put("isLogonSucessful",false);
                return resultJson;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
