package com.example.websocketJSONdemo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.stereotype.Component;

@Component
public class WebSocketServerHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // 接收到的消息转换为 JSON 对象
        String payload = message.getPayload();
        Message receivedMessage = objectMapper.readValue(payload, Message.class);
        System.out.println("Received message: " + receivedMessage);

        // 构造一个响应消息
        Message responseMessage = new Message("server", "Hello " + receivedMessage.getContent());
        String responsePayload = objectMapper.writeValueAsString(responseMessage);

        // 将 JSON 格式的响应消息发送回客户端
        session.sendMessage(new TextMessage(responsePayload));
    }
}
