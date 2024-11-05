package com.example.websocketdemo;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final WebSocketServer webSocketServer;

    public WebSocketConfig(WebSocketServer webSocketServer) {
        this.webSocketServer = webSocketServer;
    }

    //将 webSocketServer 处理器绑定到指定的路径 /ws。
    // 这意味着当客户端连接到 ws://yourserver/ws 时，
    // webSocketServer 将处理该 WebSocket 连接。

    //setAllowedOrigins("*")：允许所有域名连接到此 WebSocket 服务
    //有点类似HTTP的路由
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketServer, "/ws").setAllowedOrigins("*");
    }
}
