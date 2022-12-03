package com.tofunmi.mitri.webservice.auth;

import com.tofunmi.mitri.usermanagement.sessiontoken.UserSessionService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

/**
 * Created By tofunmi on 15/07/2022
 */

@Component
public class CustomSessionTokenFilter extends HttpFilter {
    private final UserSessionService userSessionService;

    public CustomSessionTokenFilter(UserSessionService userSessionService) {
        this.userSessionService = userSessionService;
    }

    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        CookieManagementService.getSessionCookie(request).ifPresent(cookie -> {
            String sessionId = cookie.getValue();
            if (StringUtils.hasText(sessionId)) {
                String userId = userSessionService.getUserId(sessionId);
                if (StringUtils.hasText(userId)) {
                    Authentication auth = UsernamePasswordAuthenticationToken.authenticated(userId, null, Collections.emptyList());
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
        });
        super.doFilter(request, response, chain);
    }
}
