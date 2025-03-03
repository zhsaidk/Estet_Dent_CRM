package org.example.test_orm.exception;


public class TelephoneNumberException extends PatientException {

    public TelephoneNumberException(String message) {
        super(message);
    }

    public TelephoneNumberException(String message, Throwable cause) {
        super(message, cause);
    }

    public TelephoneNumberException(Throwable cause) {
        super(cause);
    }
}
