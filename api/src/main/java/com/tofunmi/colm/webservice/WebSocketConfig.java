package com.tofunmi.colm.webservice;

import com.tofunmi.colm.webservice.chat.OnlineChatProfilesHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
public class WebSocketConfig implements WebSocketConfigurer {

    private final OnlineChatProfilesHandler onlineChatProfilesHandler;
    private final String frontEndUrl;

    public WebSocketConfig(OnlineChatProfilesHandler onlineChatProfilesHandler, @Value("${front-end-url}") String frontEndUrl) {
        this.onlineChatProfilesHandler = onlineChatProfilesHandler;
        this.frontEndUrl = frontEndUrl;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(onlineChatProfilesHandler, "/api/chat-update")
                .setAllowedOrigins(frontEndUrl);
    }
}
