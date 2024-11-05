package com.comp4342.backend.api;

import com.comp4342.backend.database.*;
import org.json.JSONObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.sql.SQLException;


@SpringBootApplication
@ComponentScan(basePackages = {"com.comp4342.backend.database", "com.comp4342.backend.api"})
@EnableWebSocket
public class BackendAPIProvider extends TextWebSocketHandler implements WebSocketConfigurer {

    private final DatabaseOperator databaseOperator;

    public BackendAPIProvider(DatabaseOperator databaseOperator) {
        this.databaseOperator = databaseOperator;
    }

    // 启动服务器的 main 方法
    public static void main(String[] args) {
        SpringApplication.run(BackendAPIProvider.class, args);
    }

    // 配置 WebSocket 处理程序
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        try {
            registry.addHandler(BackendAPIProvider(), "/ws").setAllowedOrigins("*");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    // 将 WebSocket 处理程序注册为 Spring Bean
    @Bean
    public BackendAPIProvider BackendAPIProvider() throws ClassNotFoundException {
        return new BackendAPIProvider(new DatabaseOperator());
    }

    // 当收到前端 JSON 消息时触发
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        JSONObject responseJson = new JSONObject();
        try {
            // 从前端解析收到的 JSON
            JSONObject requestJson = new JSONObject(message.getPayload());
            String action = requestJson.getString("action");

            // 解析不同的请求类型，例如登录、注册、检查用户信息等
            switch (action) {
                case "register":
                    responseJson = handleRegister(requestJson);
                    break;
                case "login":
                    responseJson = handleLogin(requestJson);
                    break;
                case "checkUserInfo":
                    responseJson = handleCheckUserInfo(requestJson);
                    break;
                case "startConversation":
                    responseJson = handleStartConversation(requestJson);
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
        session.sendMessage(new TextMessage(responseJson.toString()));
    }

    // 处理注册请求
    private JSONObject handleRegister(JSONObject requestJson) {
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
    private JSONObject handleCheckUserInfo(JSONObject requestJson) throws SQLException {
        String uid = requestJson.getString("uid");
        return databaseOperator.checkUserInfoByUID(uid);
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

}
