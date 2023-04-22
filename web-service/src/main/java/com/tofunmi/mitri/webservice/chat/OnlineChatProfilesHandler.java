package com.tofunmi.mitri.webservice.chat;

import lombok.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.*;

@Component
public class OnlineChatProfilesHandler extends TextWebSocketHandler {
    Map<String, WebSocketSession> subscribedProfiles = Collections.synchronizedMap(new HashMap<>());

    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) throws Exception {
        String profileId = getProfileId(session);
        subscribedProfiles.put(profileId, session);
    }

    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession session, @NonNull CloseStatus status) throws Exception {
        subscribedProfiles.remove(getProfileId(session));
    }

    private String getProfileId(WebSocketSession session) {
        return Objects.requireNonNull(session.getUri()).getQuery().replace("profileId=", "");
    }

    public void notifyProfiles(Collection<String> profileIds) {
        for (String profileId : profileIds) {
            if (subscribedProfiles.containsKey(profileId)) {
                WebSocketSession session = subscribedProfiles.get(profileId);
                try {
                    session.sendMessage(new TextMessage("reload"));
                } catch (IOException ignored) {
                }
            }
        }
    }
}
