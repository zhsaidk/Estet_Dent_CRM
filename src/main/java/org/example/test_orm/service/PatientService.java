package org.example.test_orm.service;

import org.example.test_orm.entity.Patient;
import org.example.test_orm.repository.PatientRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

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
            patientRepository.save(patient);
//            if (!patientRepository.existsById(savedPatient.getID())) {
//                throw new RuntimeException("Patient not saved!");
//            }
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
