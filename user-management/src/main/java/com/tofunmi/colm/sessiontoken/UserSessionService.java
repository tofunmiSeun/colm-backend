package com.tofunmi.colm.sessiontoken;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created By tofunmi on 14/07/2022
 */

@Component
public class UserSessionService {
    private final Map<String, String> cache = Collections.synchronizedMap(new HashMap<>());
    private final SessionTokenRepository repository;

    public UserSessionService(SessionTokenRepository repository) {
        this.repository = repository;
    }

    public String generateForUser(String userId) {
        String sessionId = UUID.randomUUID().toString();
        String hash = hashSessionToken(sessionId);
        SessionToken sessionToken = new SessionToken(hash, userId);
        repository.save(sessionToken);
        cache.put(hash, userId);
        return sessionId;
    }

    public String getUserId(String sessionId) {
        String hash = hashSessionToken(sessionId);
        if (cache.containsKey(hash)) {
            return cache.get(hash);
        }

        Optional<SessionToken> sessionToken = repository.findById(hash);
        sessionToken.ifPresent(token -> cache.put(hash, token.getUserId()));

        return cache.get(hashSessionToken(sessionId));
    }

    public void clearSession(String sessionId) {
        String hash = hashSessionToken(sessionId);
        repository.deleteById(hash);
        cache.remove(hash);
    }

    private String hashSessionToken(String token) {
        return DigestUtils.sha256Hex(token);
    }
}
