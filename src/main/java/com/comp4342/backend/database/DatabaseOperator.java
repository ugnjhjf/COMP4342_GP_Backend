package com.comp4342.backend.database;

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
    private String cid;
    private String uid;

    //Establish connection to the database
    public DatabaseOperator() throws ClassNotFoundException {
        if (databaseConnector == null) {
            this.databaseConnector = new DatabaseConnector();
            this.connection = databaseConnector.getConnection();
            System.out.println("(Database Operator)Database connected: " + databaseConnector.getConnection());
        }
    }
    public boolean isConnectionAlive() throws SQLException {
        return !connection.isClosed();
    }

    public void reconnect() throws ClassNotFoundException {
        if (databaseConnector == null) {
            this.databaseConnector = new DatabaseConnector();
            this.connection = databaseConnector.getConnection();
            System.out.println("(Database Operator)Database Reconnected!!!!: " + databaseConnector.getConnection());
        }
    }

    public JSONObject checkUserInfoByUID(String uid) throws SQLException {
        sql = "select * from user where uid = ?;";
        try {
            stmt = databaseConnector.getConnection().prepareStatement(sql);
            stmt.setString(1, uid);
            resultSet = stmt.executeQuery();  // 执行查询

            if (resultSet.next()) {  // 判断是否有结果
                resultJson.put("uid", resultSet.getString("uid"));
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

    public JSONObject checkUserInfoByEmail(String email) throws SQLException {
        sql = "select * from user where email = ?;";
        try {
            stmt = databaseConnector.getConnection().prepareStatement(sql);
            stmt.setString(1, email);
            resultSet = stmt.executeQuery();  // 执行查询

            if (resultSet.next()) {  // 判断是否有结果
                resultJson.put("uid", resultSet.getString("uid"));
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
    public boolean insertNewFriend(String uid, String fid, String status){
        sql = "INSERT INTO friendlist (uid,fid,status) VALUES (?, ?, ?);";
        try {
            stmt = databaseConnector.getConnection().prepareStatement(sql);
            stmt.setString(1, uid);
            stmt.setString(2, fid);
            stmt.setString(3, status);
            stmt.executeUpdate();  // 执行更新
            return true;
        }catch (SQLIntegrityConstraintViolationException e){
            System.out.println("Error: Friend request already send!");
            return false;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean updateFriendRequest(String uid, String fid, String status){
        sql = "UPDATE friendlist SET status = ? WHERE uid = ? AND fid = ?;";
        try {
            stmt = databaseConnector.getConnection().prepareStatement(sql);
            stmt.setString(1, status);
            stmt.setString(2, uid);
            stmt.setString(3, fid);
            stmt.executeUpdate();  // 执行更新
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String generateUID() {
        return java.util.UUID.randomUUID().toString();
    }
    public boolean insertRegister(String uname, String email, String password)  {
        sql = "INSERT INTO user (uid, uname, email, password) VALUES (?, ?, ?, ?);";
        try {
            stmt = databaseConnector.getConnection().prepareStatement(sql);
            stmt.setString(1, generateUID());
            stmt.setString(2, uname);
            stmt.setString(3, email);
            stmt.setString(4, password);
            stmt.executeUpdate();  // 执行更新
            return true;
        }catch (SQLIntegrityConstraintViolationException e){
            System.out.println("Error: Duplicate email");
            return false;
        }
        catch (SQLException e) {
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

    public String selectExistConversation(String uid1, String uid2){
        sql = "SELECT cid FROM conversations WHERE cid IN (SELECT cid FROM user_conversations WHERE uid1 = ?) AND cid IN (SELECT cid FROM user_conversations WHERE uid2 = ?);";
        try {
            stmt = databaseConnector.getConnection().prepareStatement(sql);
            stmt.setString(1, uid1);
            stmt.setString(2, uid2);
            resultSet = stmt.executeQuery();  // 执行查询
            if (resultSet.next()) {  // 判断是否有结果
                return resultSet.getString("cid");
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String insertStartNewConversation(String uid1, String uid2) {
        try {
            //检查是否已经存在对话
            String existConversation = selectExistConversation(uid1, uid2);
            System.out.println("existConversation: " + existConversation);
            if (existConversation != null)
            {
                System.out.println("Conversation exist");
                return existConversation;
            }

            //创建新的对话
            sql = "INSERT INTO conversations (cid) VALUES (?);";
            stmt = databaseConnector.getConnection().prepareStatement(sql);
            cid = generateUID();
            stmt.setString(1, cid);
            stmt.executeUpdate();

            //将用户绑定到对话
            sql = "INSERT INTO user_conversations (cid,uid1,uid2) VALUES (?, ?, ?);";
            stmt = databaseConnector.getConnection().prepareStatement(sql);
            stmt.setString(1, cid);
            stmt.setString(2, uid1);
            stmt.setString(3, uid2);
            stmt.executeUpdate();
            return cid;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
