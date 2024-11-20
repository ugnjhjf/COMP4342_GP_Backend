package com.example.websocketdemo;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.CopyOnWriteArraySet;

@EnableScheduling
@Component
public class WebSocketServer extends TextWebSocketHandler {

    // 用来存放每个客户端对应的 WebSocketSession 对象
    private final CopyOnWriteArraySet<WebSocketSession> sessions = new CopyOnWriteArraySet<>();

    //将客户连接的 WebSocketSession 对象存入 sessions
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
        session.sendMessage(new TextMessage("连接成功，欢迎使用 WebSocket!"));
    }

    //客户断开连接，从 sessions 移除对应的 WebSocketSession 对象
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
    }

    //定时推送
    @Scheduled(fixedRate = 1000) // 每隔5秒推送一次消息
    public void sendPeriodicMessages() {
        for (WebSocketSession session : sessions) {
            try {
                if (session.isOpen()) {
                    LocalDateTime now = LocalDateTime.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    String formattedDateTime = now.format(formatter);
                    session.sendMessage(new TextMessage("服务器推送的消息："
                            + "当前时间: "
                            + formattedDateTime));                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
