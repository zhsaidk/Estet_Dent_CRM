package org.example.test_orm.entity;

import jakarta.persistence.*;

@Entity
public class CompletedWork {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long CompletedWorkID;

    @OneToOne
    private Teeth teeth;

    @OneToOne
    private Anamnesis anamnesis;


}
