package org.example.test_orm.config;

import lombok.RequiredArgsConstructor;
import org.example.test_orm.controller_advice.CustomAuthenticationFailureHandler;
import org.example.test_orm.service.TokenService;
import org.example.test_orm.util.JwtRequestFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtRequestFilter jwtRequestFilter;
    private final AuthenticationProvider authenticationProvider;
    private final TokenService tokenService;
    private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

    @Bean
    public AuthenticationManager authenticationManager(){
        return new ProviderManager(List.of(authenticationProvider));
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth->auth
                        .requestMatchers("/login", "/js/**", "/css/**","/register", "/logout").permitAll()
                        .requestMatchers("/patients/**", "/materials/**", "/refill/**",  "/history/**").hasAuthority("DOCTOR"))
                .formLogin(login->login.loginPage("/login")
                        .defaultSuccessUrl("/patients")
                        .successHandler((request, response, authentication) -> {
                            String username = authentication.getName();
                            tokenService.setAuthCookies(response, username);
                            response.sendRedirect("/patients");
                        })
                        .failureHandler(customAuthenticationFailureHandler)
                        .permitAll())
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
