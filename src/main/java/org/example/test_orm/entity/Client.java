package org.example.test_orm.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long clientID;

    @Column(nullable = false)
    private String clientName;

    @Column(nullable = false)
    private LocalDateTime birthDate;

    @Column(nullable = false, unique = true)
    private String telephoneNumber;

//    private String gender;      // Пол
//
//    private String address;     // Адрес
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
