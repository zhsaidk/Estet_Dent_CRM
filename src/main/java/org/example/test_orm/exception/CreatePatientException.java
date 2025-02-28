package org.example.test_orm.exception;

public abstract class CreatePatientException extends RuntimeException {

    public CreatePatientException(String message) {
        super(message);
    }
    public CreatePatientException(String message, Throwable cause) {
        super(message, cause);
    }
    public CreatePatientException(Throwable cause) {
        super(cause);
    }


}
