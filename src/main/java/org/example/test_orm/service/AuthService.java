package org.example.test_orm.service;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.test_orm.entity.Doctor;
import org.example.test_orm.entity.Token;
import org.example.test_orm.exception.DoctorSaveException;
import org.example.test_orm.exception.DuplicateDoctorException;
import org.example.test_orm.repository.DoctorRepository;
import org.example.test_orm.repository.TokenRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;


@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService{
    private final DoctorRepository doctorRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final CookieService cookieService;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Doctor doctor = getDoctorByLogin(login);
        User user = new User(login, doctor.getPassword(), Set.of(doctor.getRole()));
        log.info("Загрузка найденного пользователя в UserDetails");
        return user;
    }

    public void saveDoctor(Doctor doctor) {
        try {
            doctor.setPassword(passwordEncoder.encode(doctor.getPassword()));
            doctorRepository.save(doctor);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateDoctorException(e.getMessage());
        } catch (Exception e) {
            throw new DoctorSaveException("Не удалось сохранить данные");
        }
    }

    public Token createTokenByDoctor(Doctor doctor) {
        String username = doctor.getLogin();
        return new Token(tokenService.generateAccessToken(username),
                tokenService.generateRefreshToken(username), doctor);
    }

    public void saveToken(Token token) {
        tokenRepository.save(token);
    }

    @Transactional
    public Token refreshTokens(Doctor doctor, String refreshToken) {
        String username = doctor.getLogin();
        Optional<List<Token>> optionalTokens = tokenRepository.findTokensByDoctorLogin(username);
        if (optionalTokens.isPresent()) {
            for (Token token: optionalTokens.get()) {
                if(token.getRefreshToken().equals(refreshToken)) {
                    tokenRepository.delete(token);
                    Token newToken = createTokenByDoctor(doctor);
                    tokenRepository.save(newToken);
                    return newToken;
                }
            }
        }
        log.warn("Токен пользователя: {} отличается от сохраненного", username);
        throw new JwtException("Токен пользователя: " + username + " отличается от сохраненного");
    }

    public String parseToken(String token) {
        return tokenService.parseToken(token);
    }

    public void setAuthCookies(HttpServletResponse response, Token token) {
        cookieService.setAuthCookies(response, token);
    }

    public void clearTokenCookie(HttpServletResponse response) {
        cookieService.clearTokenCookie(response);
    }

    public Doctor getDoctorByLogin(String login) {
        Optional<Doctor> optionalDoctor = doctorRepository.findDoctorByLogin(login);
        if (optionalDoctor.isEmpty()) {
            log.warn("Пользователь не найден");
            throw new UsernameNotFoundException("Доктор не найден");
        }
        return optionalDoctor.get();
    }

    public boolean isLoginTaken(String login){
        return doctorRepository.findDoctorByLogin(login).isPresent();
    }
}
