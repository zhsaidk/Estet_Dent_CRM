package org.example.test_orm.exception;

public class PatientNotFoundException extends PatientException {
    public PatientNotFoundException(String message) {
        super(message);
    }
}
