package com.tofunmi.mitri.webservice;

import lombok.NonNull;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
public class ChatSocketHandler extends TextWebSocketHandler {
    Map<String, WebSocketSession> map = Collections.synchronizedMap(new HashMap<>());

    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) throws Exception {
        String profileId = getProfileId(session);
        map.put(profileId, session);
    }

    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession session, @NonNull CloseStatus status) throws Exception {
        map.remove(getProfileId(session));
    }

    private String getProfileId(WebSocketSession session) {
        return Objects.requireNonNull(session.getUri()).getQuery().replace("profileId=", "");
    }

    @Scheduled(fixedRate = 6_000)
    public void doSomethingToConnectedSessions() throws IOException {
        for (var session : map.values()) {
            if (session.isOpen()) {
                session.sendMessage(new TextMessage("Hi, how may we help you?"));
            }
        }
    }
}
