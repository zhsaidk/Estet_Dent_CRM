package org.example.test_orm.config;

import lombok.RequiredArgsConstructor;
import org.example.test_orm.entity.Doctor;
import org.example.test_orm.entity.Token;
import org.example.test_orm.service.AuthService;
import org.example.test_orm.util.AuthFilter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final UserDetailsService userDetailsService;
    private final AuthService authService;


    @Bean
    public AuthFilter authFilter() {
        return new AuthFilter(userDetailsService, authService);
    }

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
                        .successHandler((request, response, authentication) -> {
                            Doctor doctor = authService.getDoctorByLogin(authentication.getName());
                            Token token = authService.createTokenByDoctor(doctor);
                            authService.saveToken(token);
                            authService.setAuthCookies(response, token);
                            response.sendRedirect(request.getContextPath() + "/patients");
                        })
                        .failureUrl("/login?error_message=true").permitAll()
                )
                .addFilterBefore(authFilter(), UsernamePasswordAuthenticationFilter.class)
                .logout(logout -> logout
                        .logoutUrl("/logout")).build();

    }
}
