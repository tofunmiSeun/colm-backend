package com.tofunmi.mitri.webservice;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class ChatSocketHandler extends TextWebSocketHandler {
    Map<String, WebSocketSession> map = new HashMap<>();

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        String x = new String(message.asBytes());
        map.put(x, session);
    }

    @Scheduled(fixedRate = 6_000)
    public void runnAh() throws IOException {
        for (var x : map.keySet()) {
            map.get(x).sendMessage(new TextMessage("Hi, how may we help you?"));
        }
    }
}
