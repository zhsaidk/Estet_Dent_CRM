package org.example.test_orm.util;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.test_orm.entity.Token;
import org.example.test_orm.service.CookieService;
import org.example.test_orm.service.TokenService;
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
    private final UserDetailsService userDetailsService;
    private final TokenService tokenService;
    private final CookieService cookieService;

    private static final List<String> MANE_OF_TOKENS = Arrays.asList("access_token", "refresh_token");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        Map<String, String> map = new HashMap<>();
        Cookie[] cookies = request.getCookies();

        if(cookies == null) {
            log.info("Cookies is null");
        }   else {
            for(Cookie cookie: cookies) {
                String cookieName = cookie.getName();
                if(MANE_OF_TOKENS.contains(cookie.getName())) {
                    map.put(cookieName, cookie.getValue());
                }
            }
            if (map.containsKey(MANE_OF_TOKENS.get(0))) {
                String username = tokenService.parseToken((map.get(MANE_OF_TOKENS.get(0))));
                log.info("username from access_token = {}", username);
                authenticateUser(username, request);

            }   else if(map.containsKey(MANE_OF_TOKENS.get(1))) {
                String username = tokenService.parseToken((map.get(MANE_OF_TOKENS.get(1))));
                log.info("username from refresh_token= {}", username);
                cookieService.setAuthCookies(response, new Token(tokenService.generateAccessToken(username), tokenService.generateRefreshToken(username)));
                authenticateUser(username, request);
            }
        }
        filterChain.doFilter(request, response);
    }


    private void authenticateUser(String username, HttpServletRequest request) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
