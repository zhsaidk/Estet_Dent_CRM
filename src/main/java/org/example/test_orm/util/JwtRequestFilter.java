package org.example.test_orm.util;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {
    private final JwtTokenUtil jwtTokenUtil;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {

        Cookie[] cookies = request.getCookies();
        String accessToken = null;
        String refreshToken = null;

        for (Cookie cookie : cookies) {
            log.info("Cookie name = {}", cookie.getName());
            log.info("Cookie value = {}", cookie.getValue());
//            if ("access_token".equals(cookie.getName())) {
//                accessToken = cookie.getValue();
//            } else if ("refresh_token".equals(cookie.getName())) {
//                refreshToken = cookie.getValue();
//            }
        }

//        if (accessToken != null) {
//            try {
//                String username = jwtTokenUtil.getUsernameFromToken(accessToken);
//                authenticateUser(username, request);
//            } catch (Exception e) {
//                log.warn("Access token is invalid or expired: {}", e.getMessage());
//
//                if (refreshToken != null) {
//                    try {
//                        String username = jwtTokenUtil.getUsernameFromToken(refreshToken);
//                        String newAccessToken = jwtTokenUtil.generateAccessToken(username);
//
//                        Cookie newAccessCookie = new Cookie("access_token", newAccessToken);
//                        newAccessCookie.setHttpOnly(true);
//                        newAccessCookie.setPath("/");
//                        response.addCookie(newAccessCookie);
//
//                        authenticateUser(username, request);
//
//                    } catch (Exception ex) {
//                        log.warn("Refresh token is invalid or expired: {}", ex.getMessage());
//                        clearCookies(response);
//                    }
//                } else {
//                    clearCookies(response);
//                }
//            }
//        }

        chain.doFilter(request, response);
    }

    private void authenticateUser(String username, HttpServletRequest request) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private void clearCookies(HttpServletResponse response) {
        Cookie accessTokenCookie = new Cookie("access_token", "");
        accessTokenCookie.setMaxAge(0);
        accessTokenCookie.setPath("/");
        response.addCookie(accessTokenCookie);

        Cookie refreshTokenCookie = new Cookie("refresh_token", "");
        refreshTokenCookie.setMaxAge(0);
        refreshTokenCookie.setPath("/");
        response.addCookie(refreshTokenCookie);

        SecurityContextHolder.clearContext();
    }

}