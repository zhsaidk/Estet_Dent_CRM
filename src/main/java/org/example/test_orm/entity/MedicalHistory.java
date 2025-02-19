package org.example.test_orm.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class MedicalHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long MedicalHistoryID;

    @ManyToOne
    private Client medicalHistoryOfClients;

    @ManyToOne
    private Doctor doctorMedicalHistory;

    @OneToMany
    private List<Visits> visitsOfMedicalHistory;

    private String complaints;

}
