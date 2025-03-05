package org.example.test_orm.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.test_orm.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final JwtTokenUtil jwtTokenUtil;

    @Value("${COOKIE_ACCESS_TOKEN_MAX_AGE}")
    private final int COOKIE_ACCESS_TOKEN_MAX_AGE;

    @Value("${COOKIE_REFRESH_TOKEN_MAX_AGE}")
    private final int COOKIE_REFRESH_TOKEN_MAX_AGE;

    public void setAuthCookies(HttpServletResponse response, String username) {
        String accessToken = jwtTokenUtil.generateAccessToken(username);
        String refreshToken = jwtTokenUtil.generateRefreshToken(username);

        response.addCookie(createCookie("access_token", accessToken, COOKIE_ACCESS_TOKEN_MAX_AGE));
        response.addCookie(createCookie("refresh_token", refreshToken, COOKIE_REFRESH_TOKEN_MAX_AGE));
    }

    public void clearAuthCookies(HttpServletResponse response) {
        Cookie accessCookie = new Cookie("access_token", null);
        accessCookie.setMaxAge(0);
        accessCookie.setPath("/");

        Cookie refreshCookie = new Cookie("refresh_token", null);
        refreshCookie.setMaxAge(0);
        refreshCookie.setPath("/");

        response.addCookie(accessCookie);
        response.addCookie(refreshCookie);
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
