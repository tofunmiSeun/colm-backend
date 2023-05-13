package com.tofunmi.colm.webservice.auth;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.util.Objects;
import java.util.Optional;

/**
 * Created By tofunmi on 15/07/2022
 */
public class CookieManagementService {

    public static final String cookieKey = "app-ck";

    public static Optional<Cookie> getSessionCookie(HttpServletRequest servletRequest) {
        if (servletRequest.getCookies() != null) {
            for (Cookie cookie : servletRequest.getCookies()) {
                if (Objects.equals(cookie.getName(), cookieKey)) {
                    return Optional.of(cookie);
                }
            }
        }
        return Optional.empty();
    }

    public static Cookie createLoginSessionCookie(String sessionId) {
        Cookie cookie = new Cookie(cookieKey, sessionId);
        cookie.setMaxAge((int) Duration.ofDays(365).toSeconds());
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        //cookie.setSecure(false);
        return cookie;
    }

    public static Cookie createInvalidCookie(Cookie cookie) {
        Cookie invalidCookie = new Cookie(cookieKey, null);
        invalidCookie.setMaxAge(0);
        invalidCookie.setPath(cookie.getPath());
        invalidCookie.setHttpOnly(cookie.isHttpOnly());
        invalidCookie.setSecure(cookie.getSecure());
        return invalidCookie;
    }
}
