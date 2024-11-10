package com.comp4342.backend.api;

import com.comp4342.backend.database.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Calendar;


@SpringBootApplication
@ComponentScan(basePackages = {"com.comp4342.backend.database", "com.comp4342.backend.api"})
@EnableWebSocket
public class BackendAPIProvider extends TextWebSocketHandler implements WebSocketConfigurer {

    private DatabaseOperator databaseOperator;
    private WebSocketSession session;
    private String action;


    public BackendAPIProvider() throws ClassNotFoundException {
        this.databaseOperator = new DatabaseOperator();
    }

    public BackendAPIProvider BackendAPIProvider() throws ClassNotFoundException {
        return new BackendAPIProvider();
    }

    public static void main(String[] args) {
        SpringApplication.run(BackendAPIProvider.class, args);
    }
    // 启动服务器
    // 将 WebSocket 处理程序注册为 Spring Bean


    // WebSocket 的路由（访问地址）
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(this, "/ws").setAllowedOrigins("*");  // 使用已注入的单例实例
    }


    // 当收到前端 JSON 消息时触发
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        JSONObject responseJson = new JSONObject();
        JSONArray responseJsonArray = new JSONArray();
        boolean responseJsonArrayFlag = false;
        this.session = session;

        try {
            responseJsonArrayFlag = false;
            // 从前端解析收到的 JSON
            JSONObject requestJson = new JSONObject(message.getPayload());
            // 取出action（请求什么指令）
            action = requestJson.getString("action");
            //数据库连接判断
            Calendar now = Calendar.getInstance();
            System.out.println("[→]REQ: " + action + "  " + "from: " + session.getId() + " Time: " + now.getTime());
            if (!databaseOperator.isConnectionAlive()) {
                databaseOperator.reconnect();
            }

            // 解析不同的请求类型，例如登录、注册、检查用户信息等
            switch (action) {
                //登录相关
                case "register":
                    responseJson = handleRegister(requestJson); //insertRegisterUser()
                    break;

                case "login":
                    responseJson = handleLogin(requestJson); //login()
                    break;

//                case "logout":
//                responseJson = handleLogout(requestJson);
//                break;
                //个人信息变更
                case "changeName":
                    responseJson = handleChangeName(requestJson);
                    break;

                case "changePassword":
                    responseJson = handleChangePassword(requestJson);

                    break;

                //个人资料查询
                case "checkUserInfoByUID":
                    responseJson = handleCheckUserInfoByUID(requestJson);
                    break;
                case "checkUserInfoByEmail":
                    responseJson = handleCheckUserInfoByEmail(requestJson);
                    break;

                case "checkUserIsOnline":
                    responseJson = handleCheckUserIsOnline(requestJson);
                    break;
                    //对话相关
                case "startConversation":
                    responseJson = handleStartConversation(requestJson);
                    break;
                case "sendNewMessage":
                    responseJson = handleSendNewMessage(requestJson);
                    handleNewMessageBroadcast(requestJson.getString("action"));
                    break;
                case "checkConversation":
                    responseJson = handleCheckConversation(requestJson.getString("uid"), requestJson.getString("fid"));
                    break;
                case "getAllMessage":
                    responseJsonArray = handleGetAllMessage(requestJson);
                    responseJsonArrayFlag = true;
                    break;

                case "getLatestMessage":
                    responseJson = handleGetLatestMessage(requestJson);
                    break;
                //好友相关
                case "checkUserFriendList":
                    responseJsonArray = handleCheckUserFriendList(requestJson);
                    responseJsonArrayFlag = true;
                    break;

                case "checkIsFriend":
                    responseJson = handleCheckIsFriend(requestJson);
                    break;

                default:
                    responseJson.put("error", "Unknown action");
                    break;
            }
        } catch (Exception e) {
            responseJson.put("error", "Server error: " + e.getMessage());
            e.printStackTrace();
        }

        // 发送响应 JSON 到前端
        if(responseJsonArrayFlag) {
            System.out.println("[←]RESPOND: "+ "to: " + session.getId() +" " + responseJson.toString() );
            session.sendMessage(new TextMessage(responseJsonArray.toString()));}
        else{
        System.out.println("[←]RESPOND: "+ "to: " + session.getId() +" " + responseJson.toString() );
        session.sendMessage(new TextMessage(responseJson.toString()));}
    }

    private JSONArray handleCheckUserFriendList(JSONObject requestJson) throws SQLException {
        String uid = requestJson.getString("uid");
        JSONArray response = new JSONArray();
        JSONObject action = new JSONObject();
        action.put("friendList", databaseOperator.checkUserFriendlist(uid));
        action.put("action", "checkUserFriendList");
        response.put(action);
        return response;
    }

    private JSONObject handleCheckIsFriend(JSONObject requestJson) throws SQLException {
        String uid = requestJson.getString("uid");
        String fid = requestJson.getString("fid");
        JSONObject response = new JSONObject();
        response.put("action", "checkIsFriend");
        response.put("isFriend", databaseOperator.checkIsFriend(uid, fid));
        return response;
    }

    private JSONObject handleGetLatestMessage(JSONObject requestJson) throws SQLException {
        String uid = requestJson.getString("uid");
        String fid = requestJson.getString("fid");
        JSONObject responseJson = new JSONObject();
        responseJson.put("action", "getLatestMessage");
        responseJson.put("message", databaseOperator.checkLatestMessage(uid, fid));
        return responseJson;
    }

    private JSONObject handleCheckUserIsOnline(JSONObject requestJson) {
        String uid = requestJson.getString("uid");
        JSONObject response = new JSONObject();
        response.put("action", "checkUserIsOnline");
        response.put("isOnline", databaseOperator.checkUserIsOnline(uid));
        return response;
    }

    private JSONObject handleChangePassword(JSONObject requestJson) {
        String uid = requestJson.getString("uid");
        String oldPassword = requestJson.getString("oldPassword");
        if(databaseOperator.isPasswordMatch(uid, oldPassword)){
            String newPassword = requestJson.getString("newPassword");
            boolean result = databaseOperator.changePassword(uid, newPassword);
            JSONObject response = new JSONObject();
            response.put("action", "changePassword");
            response.put("success", result);
            return response;
        }else{
            JSONObject response = new JSONObject();
            response.put("action", "changePassword");
            response.put("success", false);
            return response;
        }
    }

    private JSONObject handleChangeName(JSONObject requestJson) {
        String uid = requestJson.getString("uid");
        String newName = requestJson.getString("newName");
        boolean result = databaseOperator.changeName(uid, newName);
        JSONObject response = new JSONObject();
        response.put("action", "changeName");
        response.put("success", result);
        return response;
    }

//    private JSONObject handleLogout(JSONObject requestJson) {
//        String uid = requestJson.getString("uid");
//        boolean result = databaseOperator.logout(uid);
//        JSONObject response = new JSONObject();
//        response.put("action", "logout");
//        response.put("success", result);
//        return response;
//    }

    private JSONArray handleGetAllMessage(JSONObject requestJson) throws SQLException {
        String uid = requestJson.getString("uid");
        String fid = requestJson.getString("fid");
        JSONArray response = new JSONArray();
        JSONObject action = new JSONObject();
        action.put("allMessage", databaseOperator.checkAllMessage(uid, fid));
        action.put("action", "getAllMessage");
        response.put(action);
        return response;
    }

    private JSONObject handleSendNewMessage(JSONObject requestJson) throws SQLException {
        String uid = requestJson.getString("uid");
        String fid = requestJson.getString("fid");
        String content = requestJson.getString("content");
        Date date = new Date(System.currentTimeMillis());
        boolean result =  databaseOperator.insertNewMessage(uid, fid, content);
        JSONObject response = new JSONObject();
        response.put("action", "sendNewMessage");
        response.put("success", result);
        return response;
    }
    public JSONObject handleCheckConversation(String uid, String fid) throws SQLException {
        String cid = databaseOperator.checkConversation(uid, fid);
        JSONObject response = new JSONObject();
        response.put("action", "checkConversation");
        response.put("cid", cid);
        return response;
    }

    // 处理注册请求
    private JSONObject handleRegister(JSONObject requestJson) throws SQLException {
        String uname = requestJson.getString("uname");
        String email = requestJson.getString("email");
        String password = requestJson.getString("password");
        boolean success = databaseOperator.insertRegister(uname, email, password);

        JSONObject response = new JSONObject();
        response.put("action", "register");
        response.put("success", success);
        return response;
    }

    // 处理登录请求
    private JSONObject handleLogin(JSONObject requestJson) throws SQLException {
        String email = requestJson.getString("email");
        String password = requestJson.getString("password");
        return databaseOperator.login(email, password);
    }

    // 处理检查用户信息请求
    private JSONObject handleCheckUserInfoByUID(JSONObject requestJson) throws SQLException {
        String uid = requestJson.getString("uid");
        return databaseOperator.checkUserInfoByUID(uid);
    }

    private JSONObject handleCheckUserInfoByEmail(JSONObject requestJson) throws SQLException {
        String email = requestJson.getString("email");
        return databaseOperator.checkUserInfoByEmail(email);
    }

    // 处理开始新会话请求
    private JSONObject handleStartConversation(JSONObject requestJson) {
        String uid1 = requestJson.getString("uid1");
        String uid2 = requestJson.getString("uid2");
        String conversationId = databaseOperator.insertStartNewConversation(uid1, uid2);

        JSONObject response = new JSONObject();
        response.put("action", "startConversation");
        response.put("conversationId", conversationId);
        return response;
    }

    //通知所有客户端有新消息cid, sid, content
    private void handleNewMessageBroadcast(String client_action) throws IOException {
        JSONObject responseJSON = new JSONObject();
        responseJSON.put("action", "serverPush");
        responseJSON.put("client_action",client_action);
        responseJSON.put("uid", "1");
        responseJSON.put("fid", "2");

        System.out.println("[←]RESPOND: Broadcast new message to ALL clients");
        this.session.sendMessage(new TextMessage(responseJSON.toString()));
    }
}