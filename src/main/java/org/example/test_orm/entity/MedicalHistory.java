package org.example.test_orm.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
public class MedicalHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long MedicalHistoryID;

    @ManyToOne
    private Patient medicalHistoryOfClients;

    @ManyToOne
    private Doctor doctorMedicalHistory;

    @OneToMany
    private List<Visits> visitsOfMedicalHistory;

    @NotBlank(message = "Поля причина обращение не должен быть пустым")
    private String complaints;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date;

}
