package org.example.test_orm.exception;

import com.google.i18n.phonenumbers.NumberParseException;

public class TelephoneNumberException extends RuntimeException {

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
