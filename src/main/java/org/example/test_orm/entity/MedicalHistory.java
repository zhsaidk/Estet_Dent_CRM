package org.example.test_orm.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

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
    @JoinColumn(name = "medical_history_of_clients_id", nullable = false)
    private Patient medicalHistoryOfClients;

    @ManyToOne
    private Doctor doctorMedicalHistory;

    @OneToMany
    private List<Visits> visitsOfMedicalHistory;

    @NotBlank(message = "Причина не должна быть null")
    private String complaints;

    @NotNull(message = "Дата не должна быть null")
    private LocalDate date;

}
