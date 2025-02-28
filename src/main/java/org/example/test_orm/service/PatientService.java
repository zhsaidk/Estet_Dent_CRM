package org.example.test_orm.service;

import org.example.test_orm.entity.Patient;
import org.example.test_orm.exception.CreateDataOfBirthPatient;
import org.example.test_orm.exception.CreatePatientException;
import org.example.test_orm.repository.PatientRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.TemporalField;
import java.util.List;
import java.util.Optional;
@Service
public class PatientService {

    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public List<Patient> getPatients() {
        return patientRepository.findAll();
    }

    public Patient getPatient(long id) {
        Optional<Patient> optPatient = patientRepository.findById(id);
        if(optPatient.isPresent()) {
            return optPatient.get();
        }   else {
            throw new RuntimeException("Patient not found!");
        }
    }

    public void createPatient(Patient patient) {
        try {
            if(LocalDate.now().isAfter(patient.getBirthDate())) {
                patientRepository.save(patient);
            }   else {
                throw new CreateDataOfBirthPatient("Дата рождения пациента не может быть позже текущего даты");
            }
        }   catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException(e.getMessage());
        }


    }

    public void deletePatient(long id) {
        patientRepository.deleteById(id);
        if (patientRepository.existsById(id)) {
            throw new RuntimeException("Patient not deleted!");
        }
    }
}
