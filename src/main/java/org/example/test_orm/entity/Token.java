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
//        this.doctor = doctor;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Transient
    private String accessToken;
    @Column(unique = true)
    private String refreshToken;

//    @ManyToOne()
//    @JoinColumn(name = "user_id")
//    private Doctor doctor;


}
