package com.comp4342.frontend.api;

import org.json.JSONObject;
import org.springframework.messaging.Message;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.websocket.*;
import java.net.URI;

@ClientEndpoint(decoders = MessageDecoder.class)
public class WebSocketClient {

    private Session userSession = null;

    public WebSocketClient(URI endpointURI) {
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.connectToServer(this, endpointURI);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnOpen
    public void onOpen(Session userSession) {
        System.out.println("Connected to server");
        this.userSession = userSession;
    }

    @OnMessage
    public void onMessage(TextMessage message) {
        System.out.println("Received response: " + message);
    }

    @OnClose
    public void onClose(Session userSession, CloseReason reason) {
        System.out.println("Disconnected from server: " + reason);
        this.userSession = null;
    }

    public void sendMessage(String message) {
        this.userSession.getAsyncRemote().sendText(message);
    }


    public static void main(String[] args) throws InterruptedException {
        // 连接到 WebSocket 服务器
        WebSocketClient client = new WebSocketClient(URI.create("ws://localhost:8080/ws"));

        // 构造 JSON 格式的 `register` 请求
        JSONObject registerRequest = new JSONObject();
        registerRequest.put("action", "register");
        registerRequest.put("uname", "lad8yg8uyg");
        registerRequest.put("email", "monika@uvuv.com");
        registerRequest.put("password", "yourMonika");

        // 发送 `register` 请求
        client.sendMessage(registerRequest.toString());
//
        Thread.sleep(1000);
    }
}

