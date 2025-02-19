package org.example.test_orm.entity.Presets;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class TherapyList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long TherapyListID;
}
