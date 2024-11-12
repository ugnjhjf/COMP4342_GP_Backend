package com.comp4342.backend.api;

import com.comp4342.backend.database.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.Async;
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

    public static void main(String[] args) {
        SpringApplication.run(BackendAPIProvider.class, args);
    }

    public BackendAPIProvider BackendAPIProvider() throws ClassNotFoundException {
        return new BackendAPIProvider();
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
        this.session = session;

        try {
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
                case "register":
                    responseJson = handleRegister(requestJson);
                    break;

                case "getAllMessage":
                    JSONArray responseArrayJSON = handleGetAllMessage(requestJson);
                    responseJson.put("action", "getAllMessage");
                    responseJson.put("messages", responseArrayJSON);
                    break;
                case "sendNewMessage":
                    responseJson = handleSendNewMessage(requestJson);
                    break;
                case "getLatestMessage":
                     responseJson = handleGetLatestMessage(requestJson);
                    break;

                case "getConversationID":
                    responseJson = handleCheckConversationID(requestJson.getString("uid"), requestJson.getString("fid"));
                    break;

                case "addNewFriend":
                    responseJson = handleAddNewFriend(requestJson);
                    break;

                case "isFriendRequestAccept":
                    responseJson = handleIsFriendRequestAccept(requestJson);
                    break;

                case "deleteFriend":
                    responseJson = handleDeleteFriend(requestJson);
                    break;

                case "changePassword":
                    responseJson = handleChangePassword(requestJson);
                    break;
                case "changeName":
                    responseJson = handleChangeName(requestJson);
                    break;

                case "getUserInfoByUID":
                    responseJson = handleGetUserInfoByUID(requestJson);
                    break;
                case "getUserInfoByEmail":
                    responseJson = handleGetUserInfoByEmail(requestJson);
                    break;

                case "getUserFriendList":
                    responseJson = handleGetUserFriendList(requestJson);
                    break;

                case "isUserOnline":
                    responseJson = handleIsUserOnline(requestJson);
                    break;

                case "isFriend":
                    responseJson = handleIsFriend(requestJson);
                    break;


                case "login":
                    responseJson = handleLogin(requestJson);
                    break;

                case "logout":
                    responseJson = handleLogout(requestJson);
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
        serverBroadcast(this.action);
        System.out.println("[←]RESPOND: "+ "to: " + session.getId() +" " + responseJson.toString() );
        session.sendMessage(new TextMessage(responseJson.toString()));
    }

    private JSONObject handleIsFriend(JSONObject requestJson) throws SQLException {
        JSONObject friendInfo = databaseOperator.checkUserInfoByEmail(requestJson.getString("email"));
        boolean result= databaseOperator.checkIsFriend(requestJson.getString("uid"), friendInfo.getString("email"));
        JSONObject response = new JSONObject();
        response.put("action", "isFriend");
        response.put("isFriend", result);
        return response;
    }

    private JSONObject handleIsUserOnline(JSONObject requestJson) {
        String uid = requestJson.getString("uid");
        boolean result = databaseOperator.checkUserIsOnline(uid);
        JSONObject response = new JSONObject();
        response.put("action", "isUserOnline");
        response.put("isOnline", result);
        return response;
    }

    private JSONObject handleGetUserFriendList(JSONObject requestJson) {
        return requestJson;
    }

    private JSONObject handleChangeName(JSONObject requestJson) {
        return requestJson;
    }

    private JSONObject handleChangePassword(JSONObject requestJson) {
        return requestJson;
    }

    private JSONObject handleDeleteFriend(JSONObject requestJson) {
        return requestJson;
    }

    private JSONObject handleIsFriendRequestAccept(JSONObject requestJson) {
        return requestJson;
    }

    private JSONObject handleAddNewFriend(JSONObject requestJson) throws SQLException {
        String uid = requestJson.getString("uid");
        String email = requestJson.getString("email");
        String fid = databaseOperator.checkUserInfoByEmail(email).getString("uid");
        boolean result = databaseOperator.insertNewFriend(uid, fid, "requested");
        JSONObject response = new JSONObject();
        response.put("action", "addNewFriend");
        response.put("success", result);
        return response;
    }

    private JSONObject handleGetLatestMessage(JSONObject requestJson) {
        return requestJson;
    }

    private JSONObject handleLogout(JSONObject requestJson) {
        return requestJson;
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

    private JSONArray handleGetAllMessage(JSONObject requestJson) throws SQLException {
        String cid = requestJson.getString("cid");
        JSONArray responseArray = new JSONArray();
        return responseArray;
    }


    private JSONObject handleSendNewMessage(JSONObject requestJson) {
        String uid = requestJson.getString("uid");
        String content = requestJson.getString("content");
        String cid = requestJson.getString("fid");
        Date date = new Date(System.currentTimeMillis());
        boolean result =  databaseOperator.insertNewMessage(cid, uid, content);
        JSONObject response = new JSONObject();
        response.put("action", "sendNewMessage");
        response.put("success", result);
        return response;
    }
    public JSONObject handleCheckConversationID(String uid, String fid) throws SQLException {
        String cid = databaseOperator.checkConversationID(uid, fid);
        JSONObject response = new JSONObject();
        response.put("action", "getConversationID");
        response.put("cid", cid);
        return response;
    }


    // 处理检查用户信息请求
    private JSONObject handleGetUserInfoByUID(JSONObject requestJson) throws SQLException {
        String uid = requestJson.getString("uid");
        return databaseOperator.checkUserInfoByUID(uid);
    }

    private JSONObject handleGetUserInfoByEmail(JSONObject requestJson) throws SQLException {
        String email = requestJson.getString("email");
        return databaseOperator.checkUserInfoByEmail(email);
    }


    // 处理登录请求
    private JSONObject handleLogin(JSONObject requestJson) throws SQLException {
        String email = requestJson.getString("email");
        String password = requestJson.getString("password");
        boolean result = databaseOperator.login(email, password);
        JSONObject response = new JSONObject();
        response.put("action", "login");
        response.put("success", result);
        return response ;
    }

    //通知所有客户端有新消息cid, sid, content
    private void serverBroadcast(String client_action) throws IOException {
        JSONObject responseJSON = new JSONObject();
        responseJSON.put("action", "serverPush");
        responseJSON.put("client_action",client_action);
        responseJSON.put("uid", "1");
        responseJSON.put("fid", "2");

        System.out.println("[←]RESPOND: Broadcast new message to ALL clients");
        this.session.sendMessage(new TextMessage(responseJSON.toString()));
    }

}