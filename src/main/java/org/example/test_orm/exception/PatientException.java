package org.example.test_orm.exception;

public abstract class PatientException extends RuntimeException {

    public PatientException(String message) {
        super(message);
    }
    public PatientException(String message, Throwable cause) {
        super(message, cause);
    }
    public PatientException(Throwable cause) {
        super(cause);
    }


}
