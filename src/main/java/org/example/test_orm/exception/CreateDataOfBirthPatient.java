package org.example.test_orm.exception;

public class CreateDataOfBirthPatient extends CreatePatientException {
    public CreateDataOfBirthPatient(String message) {
        super(message);
    }
    public CreateDataOfBirthPatient(String message, Throwable cause) {
        super(message, cause);
    }
    public CreateDataOfBirthPatient(Throwable cause) {
        super(cause);
    }

}
