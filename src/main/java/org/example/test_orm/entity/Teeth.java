package org.example.test_orm.entity;

import jakarta.persistence.*;

@Entity
public class Teeth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long teethID;

    private String teethCondition;
}
