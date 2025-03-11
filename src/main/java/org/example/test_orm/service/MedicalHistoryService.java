package org.example.test_orm.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.test_orm.entity.MedicalHistory;
import org.example.test_orm.repository.MedicalHistoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MedicalHistoryService {
    private final MedicalHistoryRepository medicalHistoryRepository;

    public List<MedicalHistory> getAllByClientId(Long clientId) {
        return medicalHistoryRepository.findAllByClientId(clientId);
    }

    public void create(MedicalHistory medicalHistory){
        medicalHistoryRepository.save(medicalHistory);
    }

    public boolean deleteById(Long id){  //todo Нужно создать метод в контроллере
        return medicalHistoryRepository.findById(id)
                .map(history->{
                    medicalHistoryRepository.deleteById(id);
                    return true;
                })
                .orElse(false);
    }
}
