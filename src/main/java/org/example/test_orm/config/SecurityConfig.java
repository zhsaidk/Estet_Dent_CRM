package org.example.test_orm.config;

import lombok.RequiredArgsConstructor;
import org.example.test_orm.entity.Doctor;
import org.example.test_orm.entity.Token;
import org.example.test_orm.repository.DoctorRepository;
import org.example.test_orm.service.AuthService;
import org.example.test_orm.util.AuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final UserDetailsService userDetailsService;
    private final AuthService authService;
    private final DoctorRepository doctorRepository;

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
                            String userId = authentication.getName();
//                            Doctor doc = authService.getDoctorById(userId);
                            Token token = new Token(authService.generateAccessToken(userId), authService.generateRefreshToken(userId));
//                            authService.saveRefreshToken(token);
                            authService.setAuthCookies(response, token);
                            response.sendRedirect(request.getContextPath() + "/patients");
                        })).failureUrl("/login?error_message=true").permitAll()
                )
                .addFilterBefore(new AuthFilter(userDetailsService, authService), UsernamePasswordAuthenticationFilter.class)
                .logout(logout -> logout
                        .logoutUrl("/logout")).build();

    }
}
