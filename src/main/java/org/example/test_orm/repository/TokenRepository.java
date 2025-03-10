package org.example.test_orm.repository;

import org.example.test_orm.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, Long> {
    void deleteByRefreshToken(String token);
}
