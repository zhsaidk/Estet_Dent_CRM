package org.example.test_orm.service;

import org.example.test_orm.entity.Patient;
import org.example.test_orm.exception.CreateDataOfBirthPatientException;
import org.example.test_orm.exception.PatientNotFoundException;
import org.example.test_orm.repository.PatientRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
            throw new PatientNotFoundException("Patient not found!");
        }
    }

    public void createPatient(Patient patient) {
        try {
            if(LocalDate.now().isAfter(patient.getBirthDate())) {
                patientRepository.save(patient);
            }   else {
                throw new CreateDataOfBirthPatientException("The patient's date of birth cannot be later than the current date.");
            }
        }   catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException(e.getMessage());
        }


    }

    public void deletePatient(long id) {
        patientRepository.deleteById(id);
        if (patientRepository.existsById(id)) {
            throw new PatientNotFoundException("Patient not deleted!");
        }
    }
}
