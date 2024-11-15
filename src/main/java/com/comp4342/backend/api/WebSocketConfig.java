package com.comp4342.backend.api;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final BackendAPIProvider backendAPIProvider;

    public WebSocketConfig(BackendAPIProvider backendAPIProvider) {
        this.backendAPIProvider = backendAPIProvider;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(backendAPIProvider, "/backend-api")
                .setAllowedOrigins("*"); // 设置允许跨域
    }
}
