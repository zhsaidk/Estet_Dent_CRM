package org.example.test_orm.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.test_orm.entity.Token;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CookieService {
    private final TokenService tokenService;

    @Value("${COOKIE_ACCESS_TOKEN_MAX_AGE}")
    private final int COOKIE_ACCESS_TOKEN_MAX_AGE;

    @Value("${COOKIE_REFRESH_TOKEN_MAX_AGE}")
    private final int COOKIE_REFRESH_TOKEN_MAX_AGE;

    public void setAuthCookies(HttpServletResponse response, Token token) {
        response.addCookie(createCookie("access_token", token.getAccessToken(), COOKIE_ACCESS_TOKEN_MAX_AGE));
        response.addCookie(createCookie("refresh_token", token.getRefreshToken(), COOKIE_REFRESH_TOKEN_MAX_AGE));
    }

    public void clearTokenCookie(HttpServletResponse response) {
        response.addCookie(clearCookie("access_token"));
        response.addCookie(clearCookie("refresh_token"));
    }

    private Cookie clearCookie(String cookieName) {
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        return cookie;
    }

    private Cookie createCookie(String cookieName, String token, int expires) {
        Cookie cookie = new Cookie(cookieName, token);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(expires);
        return cookie;
    }
}
