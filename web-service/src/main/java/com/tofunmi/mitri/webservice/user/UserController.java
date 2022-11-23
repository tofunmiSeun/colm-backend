package com.tofunmi.mitri.webservice.user;

import com.tofunmi.mitri.usermanagement.UserManagementService;
import com.tofunmi.mitri.usermanagement.sessiontoken.UserSessionService;
import com.tofunmi.mitri.usermanagement.UserSetupResponse;
import com.tofunmi.mitri.webservice.auth.CookieManagementService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created By tofunmi on 12/07/2022
 */

@RestController
@RequestMapping("user")
public class UserController {
    private final UserManagementService userManagementService;
    private final UserSessionService userSessionService;

    public UserController(UserManagementService userManagementService, UserSessionService userSessionService) {
        this.userManagementService = userManagementService;
        this.userSessionService = userSessionService;
    }

    @PostMapping("setup/google")
    public UserSetupResponse setup(@RequestBody SetupUserRequest request, HttpServletResponse servletResponse) {
        UserSetupResponse response = userManagementService.setupUserWithGoogleToken(request.getToken());
        String sessionId = userSessionService.generateForUser(response.getUserId());
        Cookie cookie = CookieManagementService.createLoginSessionCookie(sessionId);
        servletResponse.addCookie(cookie);
        return response;
    }

    @PostMapping("logout")
    public void logout(HttpServletRequest servletRequest, HttpServletResponse httpServletResponse) {
        invalidateSessionCookie(servletRequest, httpServletResponse);
    }

    private void invalidateSessionCookie(HttpServletRequest servletRequest, HttpServletResponse httpServletResponse) {
        CookieManagementService.getSessionCookie(servletRequest).ifPresent(cookie -> {
            String sessionId = cookie.getValue();
            userSessionService.clearSession(sessionId);
            Cookie invalidCookie = CookieManagementService.createInvalidCookie(cookie);
            httpServletResponse.addCookie(invalidCookie);
        });
    }
}
