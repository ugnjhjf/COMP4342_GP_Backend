package com.comp4342.backend.database;

import org.json.JSONArray;
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
//    public String checkConversationID(String uid, String fid) throws SQLException {
//        sql = "SELECT cid FROM user_conversations where (uid1 = ? AND uid2 = ?) OR (uid1 = ? AND uid2 = ?);";
//        try {
//            stmt = databaseConnector.getConnection().prepareStatement(sql);
//            stmt.setString(1, uid);
//            stmt.setString(2, fid);
//            resultSet = stmt.executeQuery();  // 执行查询
//            if (resultSet.next()) {  // 判断是否有结果
//                return resultSet.getString("cid");
//            } else {
//                return null;
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

    public boolean deleteFriend(String uid,String fid) throws SQLException{
        sql = "UPDATE friendlist SET status = \"blocked\" WHERE (uid = ? AND fid = ?) OR (uid = ? AND fid = ?);";
        try {
            stmt = databaseConnector.getConnection().prepareStatement(sql);
            stmt.setString(1, uid);
            stmt.setString(2, fid);
            stmt.setString(3, fid);
            stmt.setString(4, uid);
            stmt.executeUpdate();  // 执行查询
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
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


    public JSONArray checkUserFriendlist(String uid) throws SQLException {
        String sql = "SELECT DISTINCT user.uname, user.uid, friendlist.fid " +
                "FROM friendlist " +
                "JOIN user ON ( " +
                "    (friendlist.uid = ? AND friendlist.fid = user.uid) " +
                "    OR " +
                "    (friendlist.fid = ? AND friendlist.uid = user.uid) " +
                ") " +
                "WHERE friendlist.status = 'accepted'";

        JSONArray friendsList = new JSONArray();

        try (PreparedStatement stmt = databaseConnector.getConnection().prepareStatement(sql)) {
            stmt.setString(1, uid);
            stmt.setString(2, uid);

            try (ResultSet resultSet = stmt.executeQuery()) {
                while (resultSet.next()) {
                    JSONObject friend = new JSONObject();
                    String friendUid = resultSet.getString("uid");
                    String friendFid = resultSet.getString("fid");
                    String uname = resultSet.getString("uname");

                    // 判断是否为 uid 还是 fid，并获取对话 ID 和在线状态
                    String friendId = uid.equals(friendUid) ? friendFid : friendUid;
                    String cid = checkConversationID(uid, friendId);
                    boolean isOnline = checkUserIsOnline(friendId);

                    // 将好友信息、cid 和在线状态填入 JSON 对象
                    friend.put("fid", friendId);
                    friend.put("uname", uname);
                    friend.put("cid", cid);
                    friend.put("isOnline", isOnline);

                    // 将好友的信息添加到 JSONArray 中
                    friendsList.put(friend);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        return friendsList;
    }


    public boolean checkUserIsOnline(String uid) {
        sql = "SELECT status FROM user WHERE uid = ?";
        try (PreparedStatement stmt = databaseConnector.getConnection().prepareStatement(sql)) {
            stmt.setString(1, uid);
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {  // 确保有结果
                    String status = resultSet.getString("status");
                    System.out.println("User status: " + status);
                    // 判断用户状态是否为 "online"
                    return "online".equalsIgnoreCase(status);
                } else {
                    // 用户不存在
                    System.out.println("User with uid " + uid + " not found.");
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean checkIsFriend(String uid, String fid) throws SQLException{
        sql = "select uid,fid from friendlist where ((uid = ? AND fid = ?) OR (uid = ? AND fid = ?)) AND (status = \"accepted\");";
        try {
            stmt = databaseConnector.getConnection().prepareStatement(sql);
            stmt.setString(1, uid);
            stmt.setString(2, fid);
            stmt.setString(3, fid);
            stmt.setString(4, uid);
            resultSet = stmt.executeQuery();  // 执行查询
            if (resultSet.next()) {  // 判断是否有结果
                return true;  // 成功返回数据
            } else {
                return false;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean changePassword(String uid,String newPassword)   {
        sql = "UPDATE user SET password = ? WHERE uid = ?;";
        try {
            stmt = databaseConnector.getConnection().prepareStatement(sql);
            stmt.setString(1, newPassword);
            stmt.setString(2, uid);
            stmt.executeUpdate();  // 执行更新return resultJson;  // 成功返回数据
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean changeName(String uid,String newName)   {
        sql = "UPDATE user SET uname = ? WHERE uid = ?;";
        try {
            stmt = databaseConnector.getConnection().prepareStatement(sql);
            stmt.setString(1, newName);
            stmt.setString(2, uid);
            stmt.executeUpdate();  // 执行更新return resultJson;  // 成功返回数据
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

    public boolean login(String email,String password){
        sql = "SELECT uid,uname FROM user where email = ? AND password = ?;";
        try{
            stmt = databaseConnector.getConnection().prepareStatement(sql);
            stmt.setString(1,email);
            stmt.setString(2,password);
            resultSet = stmt.executeQuery();  // 执行查询
            if (resultSet.next()) {  // 判断是否有结果
               return true;
            } else {
                return false;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
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

    public String checkConversationID(String uid1, String uid2) {
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
    public boolean insertNewMessage(String cid, String sid,String content)
    {
        sql = "INSERT INTO messages (cid, sid, content) VALUES (?, ?, ?);";
        try {
            stmt = databaseConnector.getConnection().prepareStatement(sql);
            stmt.setString(1, cid);
            stmt.setString(2, sid);
            stmt.setString(3, content);
            stmt.executeUpdate();  // 执行更新
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }
    public JSONArray getAllMessage(String uid, String fid)
    {
        sql = "SELECT * FROM messages WHERE cid = (SELECT cid FROM user_conversations WHERE (uid1 = ? AND uid2 = ?) OR (uid1 = ? AND uid2 = ?));";
        try {
            stmt = databaseConnector.getConnection().prepareStatement(sql);
            stmt.setString(1, uid);
            stmt.setString(2, fid);
            stmt.setString(3, fid);
            stmt.setString(4, uid);
            resultSet = stmt.executeQuery();  // 执行查询
            JSONArray messages = new JSONArray();
            while (resultSet.next()) {
                JSONObject message = new JSONObject();
                message.put("cid", resultSet.getString("cid"));
                message.put("mid", resultSet.getString("mid"));
                message.put("sid", resultSet.getString("sid"));
                message.put("content", resultSet.getString("content"));
                message.put("timestamp", resultSet.getString("timestamp"));
                message.put("status", resultSet.getString("status"));
                messages.put(message);
            }
            return messages;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public JSONObject getLatestMessage(String uid, String fid)
    {
        sql = "SELECT content FROM messages WHERE cid = (SELECT cid FROM user_conversations WHERE (uid1 = ? AND uid2 = ?) OR (uid1 = ? AND uid2 = ?)) ORDER BY timestamp DESC LIMIT 1;";
        try {
            stmt = databaseConnector.getConnection().prepareStatement(sql);
            stmt.setString(1, uid);
            stmt.setString(2, fid);
            stmt.setString(3, fid);
            stmt.setString(4, uid);
            resultSet = stmt.executeQuery();  // 执行查询
            JSONObject response = new JSONObject();
            if (resultSet.next()) {  // 判断是否有结果
                response.put("sid", resultSet.getString("sid"));
                response.put("content", resultSet.getString("content"));
                response.put("timestamp", resultSet.getString("timestamp"));
                return response;
            } else {
                return null;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

}
