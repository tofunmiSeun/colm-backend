package com.tofunmi.mitri.webservice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
public class WebSocketConfig implements WebSocketConfigurer {

    private final ChatSocketHandler chatSocketHandler;
    private final String frontEndUrl;

    public WebSocketConfig(ChatSocketHandler chatSocketHandler, @Value("${front-end-url}") String frontEndUrl) {
        this.chatSocketHandler = chatSocketHandler;
        this.frontEndUrl = frontEndUrl;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(chatSocketHandler, "/api/chat-update")
                .setAllowedOrigins(frontEndUrl);
    }
}
