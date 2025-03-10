package org.example.test_orm.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.test_orm.entity.Token;
import org.example.test_orm.service.CookieService;
import org.example.test_orm.service.TokenService;
import org.example.test_orm.util.AuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.util.List;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final UserDetailsService userDetailsService;
    private final TokenService tokenService;
    private final CookieService cookieService;

//    private final AuthenticationProvider authenticationProvider;
//
//    @Bean
//    public AuthenticationManager authenticationManager(){
//        return new ProviderManager(List.of(authenticationProvider));
//    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/login", "/register", "/logout").permitAll()
                        .anyRequest().hasAnyAuthority("DOCTOR")
                )
                .formLogin((form) -> form
                        .loginPage("/login").permitAll()
                        .successHandler(((request, response, authentication) -> {
                            String username = authentication.getName();
                            Token token = new Token(tokenService.generateAccessToken(username), tokenService.generateRefreshToken(username));
                            cookieService.setAuthCookies(response, token);
                            SecurityContextHolder.clearContext();
                            response.sendRedirect(request.getContextPath() + "/patients");
                        })).failureUrl("/login?error=true").permitAll()
                ).addFilterBefore(new AuthFilter(userDetailsService, tokenService, cookieService), UsernamePasswordAuthenticationFilter.class)
                .logout(logout -> logout
                        .logoutUrl("logout")).build();

    }
}
