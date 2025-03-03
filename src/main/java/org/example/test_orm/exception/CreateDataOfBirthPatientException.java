package org.example.test_orm.exception;

public class CreateDataOfBirthPatientException extends PatientException {
    public CreateDataOfBirthPatientException(String message) {
        super(message);
    }
    public CreateDataOfBirthPatientException(String message, Throwable cause) {
        super(message, cause);
    }
    public CreateDataOfBirthPatientException(Throwable cause) {
        super(cause);
    }

}
