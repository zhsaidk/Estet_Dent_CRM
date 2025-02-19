package org.example.test_orm.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Visits {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long visitsID;

    @OneToMany
    private List<Document> documentList;

    @OneToMany
    private List<CompletedWork> completedWork;

}
