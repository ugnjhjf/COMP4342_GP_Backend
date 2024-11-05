package com.comp4342.frontend.api;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;

public class FrontendAPIProvider extends WebSocketClient {

    // 构造函数，初始化 WebSocket 客户端
    public FrontendAPIProvider(URI serverURI) {
        super(serverURI);
    }

    @Override
    public void onOpen(ServerHandshake handshake) {
        System.out.println("Connected to WebSocket server");

        // 示例：发送登录请求
        JSONObject loginRequest = new JSONObject();
        loginRequest.put("action", "login");
        loginRequest.put("email", "tester@gmail.com");
        loginRequest.put("password", "123456");

        send(loginRequest.toString());  // 发送 JSON 请求
        System.out.println("Sent login request: " + loginRequest);
    }

    @Override
    public void onMessage(String message) {
        // 接收并处理来自服务器的消息
        JSONObject response = new JSONObject(message);
        System.out.println("Received response: " + response);

        // 根据 action 字段判断响应类型
        String action = response.optString("action", "unknown");
        switch (action) {
            case "login":
                handleLoginResponse(response);
                break;
            case "register":
                handleRegisterResponse(response);
                break;
            default:
                System.out.println("Unhandled action: " + action);
                break;
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("Disconnected from WebSocket server: " + reason);
    }

    @Override
    public void onError(Exception ex) {
        System.err.println("WebSocket error: " + ex.getMessage());
    }

    // 处理登录响应
    private void handleLoginResponse(JSONObject response) {
        boolean isSuccessful = response.optBoolean("isLogonSucessful", false);
        if (isSuccessful) {
            System.out.println("Login successful. User ID: " + response.getString("uid"));
        } else {
            System.out.println("Login failed.");
        }
    }

    // 处理注册响应
    private void handleRegisterResponse(JSONObject response) {
        boolean success = response.optBoolean("success", false);
        if (success) {
            System.out.println("Registration successful.");
        } else {
            System.out.println("Registration failed.");
        }
    }

    // 发送注册请求示例方法
    public void sendRegisterRequest(String uname, String email, String password) {
        JSONObject registerRequest = new JSONObject();
        registerRequest.put("action", "register");
        registerRequest.put("uname", uname);
        registerRequest.put("email", email);
        registerRequest.put("password", password);

        send(registerRequest.toString());  // 发送 JSON 请求
        System.out.println("Sent register request: " + registerRequest);
    }

    public static void main(String[] args) {
        try {
            // 创建 WebSocket 客户端并连接到 WebSocket 服务器
            URI serverURI = new URI("ws://localhost:8080/backend-api");
            FrontendAPIProvider client = new FrontendAPIProvider(serverURI);
            client.connectBlocking();  // 阻塞，直到连接建立

            // 示例：发送注册请求
            client.sendRegisterRequest("testUser", "test@example.com", "password123");

        } catch (URISyntaxException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
