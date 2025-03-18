package org.example.test_orm.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@Setter
public class Token {

    public Token() {
    }

    public Token(String accessToken , String refreshToken, Doctor doctor) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.doctor = doctor;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Transient
    private String accessToken;
    @Column(unique = true)
    private String refreshToken;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_login", referencedColumnName = "login", nullable = false)
    private Doctor doctor;


}
