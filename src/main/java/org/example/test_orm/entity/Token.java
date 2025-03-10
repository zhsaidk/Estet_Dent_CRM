package org.example.test_orm.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Token {

    public Token() {
    }

    public Token(String accessToken , String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Transient
    private String accessToken;

    private String refreshToken;


}
