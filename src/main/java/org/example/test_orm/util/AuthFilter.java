package org.example.test_orm.util;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.test_orm.entity.Doctor;
import org.example.test_orm.entity.Token;
import org.example.test_orm.service.AuthService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
public class AuthFilter extends OncePerRequestFilter {
    private final static String ACCESS_TOKEN = "access_token";
    private final static String REFRESH_TOKEN = "refresh_token";

    private final UserDetailsService userDetailsService;
    private final AuthService authService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        Map<String, String> mapOfCookie = setCookieIfExist(request.getCookies());
        try {
            if (mapOfCookie.containsKey(ACCESS_TOKEN)) {
                String userId = authService.parseToken((mapOfCookie.get(ACCESS_TOKEN)));
                log.info("userID from {} = {}", ACCESS_TOKEN, userId);
                authenticateUser(userId, request);
            }   else if(mapOfCookie.containsKey(REFRESH_TOKEN)) {
                String userId = authService.parseToken(REFRESH_TOKEN);
//                Doctor doc = authService.getDoctorById(userId);
                log.info("userID from {} = {}", REFRESH_TOKEN, userId);
                Token token = new Token(authService.generateAccessToken(userId),
                        authService.generateRefreshToken(userId));
//                authService.saveRefreshToken(token);
                authService.setAuthCookies(response, token);
                authenticateUser(userId, request);
            }
            filterChain.doFilter(request, response);
        }   catch (JwtException e) {
            log.info("Invalid JWT token");
            request.setAttribute("error_message", "Доступ запрещен: неверный JWT токен.");
            request.getRequestDispatcher("/logout").forward(request, response);
        }
    }
    private Map<String, String> setCookieIfExist(Cookie[] cookies) {
        Map<String, String> map = new HashMap<>();
        if (cookies == null) {
            log.info("Cookies is null");
        } else {
            for (Cookie cookie : cookies) {
                String cookieName = cookie.getName();
                if (ACCESS_TOKEN.equals(cookie.getName()) || REFRESH_TOKEN.equals(cookie.getName())) {
                    map.put(cookieName, cookie.getValue());
                }
            }
        }
        return map;
    }

    private void authenticateUser(String username, HttpServletRequest request) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
