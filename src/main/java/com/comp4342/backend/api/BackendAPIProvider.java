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

import static java.lang.Thread.sleep;


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
                    serverBroadcast(this.action);
                    break;
                case "getLatestMessage":
                     responseJson = handleGetLatestMessage(requestJson);
                    break;

                case "getConversationIDByID":
                    responseJson = handleGetConversationIDByID(requestJson.getString("uid"), requestJson.getString("fid"));
                    break;

                case "getConversationIDByEmail":
                    responseJson = handleGetConversationIDByEmail(requestJson.getString("uid"), requestJson.getString("email"));
                    break;

                case "addNewFriend":
                    responseJson = handleAddNewFriend(requestJson);
                    serverBroadcast(this.action);
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

                    case "getFriendRequestList":
                    responseJson = handleGetFriendRequestList(requestJson);
                    break;

                case "isUserOnline":
                    responseJson = handleIsUserOnline(requestJson);
                    break;

                case "isFriendByEmail":
                    responseJson = handleIsFriendByEmail(requestJson);
                    break;

                case "isFriendByUID":
                    responseJson = handleIsFriendByUID(requestJson);
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

        System.out.println("[←]RESPOND: "+ "to: " + session.getId() +" " + responseJson.toString() );
        session.sendMessage(new TextMessage(responseJson.toString()));
    }

    private JSONObject handleIsFriendByEmail(JSONObject requestJson) throws SQLException, InterruptedException {
        System.out.println("!!! Backend Received JSON:" + requestJson.toString());
        JSONObject friendInfo = databaseOperator.checkUserInfoByEmail(requestJson.getString("email"));
        sleep(1000);
        boolean result= databaseOperator.checkIsFriend(requestJson.getString("uid"), friendInfo.getString("uid"));
        JSONObject response = new JSONObject();
        response.put("action", "isFriendByEmail");
        response.put("success", result);
        return response;
    }

    private JSONObject handleIsFriendByUID(JSONObject requestJson) throws SQLException {
        boolean result = databaseOperator.checkIsFriend(requestJson.getString("uid"), requestJson.getString("fid"));
        JSONObject response = new JSONObject();
        response.put("action", "isFriendByUID");
        response.put("success", result);
        return response;
    }

    private JSONObject handleIsUserOnline(JSONObject requestJson) {
        String uid = requestJson.getString("uid");
        boolean result = databaseOperator.checkUserIsOnline(uid);
        JSONObject response = new JSONObject();
        response.put("action", "isUserOnline");
        response.put("success", result);
        return response;
    }

    private JSONObject handleGetUserFriendList(JSONObject requestJson) throws SQLException {
        String uid = requestJson.getString("uid");
        JSONArray friendList = databaseOperator.checkUserFriendlist(uid);
        JSONObject response = new JSONObject();
        response.put("action", "getUserFriendList");
        response.put("friendList", friendList);
        return response;
    }
    private JSONObject handleGetFriendRequestList(JSONObject requestJson) throws SQLException {
        String uid = requestJson.getString("uid");
        JSONArray friendList = databaseOperator.checkFriendRequestList(uid);
        JSONObject response = new JSONObject();
        System.out.println("friendList: " + friendList);
        response.put("action", "getFriendRequestList");
        response.put("request_friendList", friendList);
        return response;
    }

    private JSONObject handleChangeName(JSONObject requestJson) {
        String uid = requestJson.getString("uid");
        String uname = requestJson.getString("uname");
        boolean result = databaseOperator.changeName(uid, uname);
        JSONObject response = new JSONObject();
        response.put("action", "changeName");
        response.put("success", result);
        return response;
    }

    private JSONObject handleChangePassword(JSONObject requestJson) {
        String uid = requestJson.getString("uid");
        String password = requestJson.getString("password");
        boolean result = databaseOperator.changePassword(uid, password);
        JSONObject response = new JSONObject();
        response.put("action", "changePassword");
        response.put("success", result);
        return response;
    }

    private JSONObject handleDeleteFriend(JSONObject requestJson) {
        String uid = requestJson.getString("uid");
        String fid = requestJson.getString("fid");
        boolean result = databaseOperator.updateFriendRequest(uid, fid,"blocked");
        JSONObject response = new JSONObject();
        response.put("action", "deleteFriend");
        response.put("success", result);
        return response;
    }

    private JSONObject handleIsFriendRequestAccept(JSONObject requestJson) throws SQLException {
        String uid = requestJson.getString("uid");
        String fid = requestJson.getString("fid");
        String status = requestJson.getString("status");
        boolean result = databaseOperator.updateFriendRequest(uid, fid, status);
        System.out.println("isFriendRequestAccept: " + result);
        JSONObject response = new JSONObject();
        response.put("action", "isFriendRequestAccept");
        response.put("success", result);
        return response;
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
        String uid = requestJson.getString("uid");
        String fid = requestJson.getString("fid");
        JSONObject response = databaseOperator.getLatestMessage(uid, fid);
        response.put("action", "getLatestMessage");
        return response;
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
        String uid = requestJson.getString("uid");
        String fid = requestJson.getString("fid");
        JSONArray responseArray = databaseOperator.getAllMessage(uid, fid);

        return responseArray;
    }


    private JSONObject handleSendNewMessage(JSONObject requestJson) {
        String uid = requestJson.getString("uid");
        String content = requestJson.getString("content");
        String cid = databaseOperator.checkConversationIDByID(uid, requestJson.getString("fid"));
        Date date = new Date(System.currentTimeMillis());
        boolean result =  databaseOperator.insertNewMessage(cid, uid, content);
        JSONObject response = new JSONObject();
        response.put("action", "sendNewMessage");
        response.put("success", result);
        return response;
    }
    public JSONObject handleGetConversationIDByEmail(String uid, String email) throws SQLException {
        String cid = databaseOperator.checkConversationIDByEmail(uid, email);
        JSONObject response = new JSONObject();
        response.put("action", "getConversationIDByEmail");
        response.put("cid", cid);
        return response;
    }

    public JSONObject handleGetConversationIDByID(String uid, String fid) throws SQLException {
        String cid = databaseOperator.checkConversationIDByID(uid, fid);
        JSONObject response = new JSONObject();
        response.put("action", "getConversationIDByID");
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
        response.put("uid", databaseOperator.checkUserInfoByEmail(email).getString("uid"));
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