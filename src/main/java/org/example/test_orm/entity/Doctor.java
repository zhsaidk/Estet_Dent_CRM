package org.example.test_orm.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long doctorID;

    @NotBlank(message = "Name is required")
    private String doctorName;

    @NotBlank(message = "Login is required")
    @Column(unique = true)
    private String login;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role = Role.DOCTOR; //Значение по умолчанию

    protected boolean canEqual(final Object other) {
        return other instanceof Doctor;
    }

}
