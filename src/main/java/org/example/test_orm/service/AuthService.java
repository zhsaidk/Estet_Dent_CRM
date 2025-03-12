package org.example.test_orm.service;

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
        String doctorId = String.valueOf(doctor.getDoctorID());
        User user = new User(doctorId, doctor.getPassword(), Set.of(doctor.getRole()));
        log.info("Загрузка найденного пользователя в UserDetails");
        return user;
    }

    public void save(Doctor doctor) {
        try {
            doctor.setPassword(passwordEncoder.encode(doctor.getPassword()));
            doctorRepository.save(doctor);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateDoctorException(e.getMessage());
        } catch (Exception e) {
            throw new DoctorSaveException("Не удалось сохранить данные");
        }
    }
//    @Transactional
//    public void saveRefreshToken(Token token) {
//        try {
//            Optional<Token> oldToken = tokenRepository.getTokenByDoctor(token.getDoctor());
//            if(oldToken.isPresent()) {
//                tokenRepository.delete(oldToken.get());
//                log.info("Удаление страого токена");
//            }
////            tokenRepository.save(token);
////            log.info("Создание нового токена");
//
//        } catch (DataIntegrityViolationException e) {
//            throw new DataIntegrityViolationException(e.getMessage());
//        }
//    }


    public void setAuthCookies(HttpServletResponse response, Token token) {
        cookieService.setAuthCookies(response, token);
    }

    public void clearTokenCookie(HttpServletResponse response) {
        cookieService.clearTokenCookie(response);
    }

    public String generateAccessToken(String userID) {
        return tokenService.generateAccessToken(userID);
    }

    public String generateRefreshToken(String userID) {
        return tokenService.generateRefreshToken(userID);
    }

    public String parseToken(String token) {
        return tokenService.parseToken(token);
    }

    private Doctor getDoctorByLogin(String login) {
        Optional<Doctor> optionalDoctor = doctorRepository.findDoctorByLogin(login);
        if (optionalDoctor.isEmpty()) {
            log.warn("Пользователь не найден");
            throw new UsernameNotFoundException("Доктор не найден");
        }
        return optionalDoctor.get();
    }

    public Doctor getDoctorById(String id) {
        Optional<Doctor> optionalDoctor = doctorRepository.findById(Long.parseLong(id));
        if (optionalDoctor.isEmpty()) {
            log.warn("Пользователь не найден");
            throw new UsernameNotFoundException("Доктор не найден");
        }
        return optionalDoctor.get();
    }

}
