package org.example.test_orm.repository;

import org.example.test_orm.entity.Material;
import org.example.test_orm.entity.MedicalHistory;
import org.example.test_orm.entity.Visits;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicalHistoryRepository extends JpaRepository<MedicalHistory, Long> {
    @EntityGraph(attributePaths = {"doctorMedicalHistory"})
    @Query("select m from MedicalHistory m where m.medicalHistoryOfClients.ID = :clientId")
    List<MedicalHistory> findAllByClientId(Long clientId);
}
