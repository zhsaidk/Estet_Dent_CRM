package org.example.test_orm.repository;

import org.example.test_orm.entity.Doctor;
import org.example.test_orm.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
//    void deleteRefreshTokenByDoctorLogin(String login);
//    Optional<Token> getTokenByRefreshToken(String token);
//    Optional<Token> getTokenByDoctor(Doctor doctor);
}
