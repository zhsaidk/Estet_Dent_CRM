package org.example.test_orm.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


@Entity
@Getter
@Setter
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long ID;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDate birthDate;

    @Column(nullable = false, unique = true)
    private String telephoneNumber;

    @Column(nullable = false)
    private String address;     // Адрес


//    private String gender;      // Пол
//
//
//    private String jobPlace;    // Место работы
//
//    private String diagnosis;   // Диагноз
//
//    private String complaints;  // Жалобы
//
//    private String previousAndConcomitantDiseases;  // Перенесеныне и сопутствующие заболевания
//
//    private String developmentPresentDisease;   // Развитие настоящего заболевания
//
//    private String objectiveDataExternalInspection; // Данные объективного исследования, внешний осмотр
//
//    private String bite;  // Прикус
//
//    private String conditionMucous; // Состояние слизистой оболочки полости рта, десен, альвеолярных отростков и нёба
//
//    private String xRay;  // Данные рентгеновских лабораторных исследований


}
