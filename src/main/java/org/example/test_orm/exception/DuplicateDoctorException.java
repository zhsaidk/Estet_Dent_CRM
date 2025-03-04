package org.example.test_orm.exception;

public class DuplicateDoctorException extends RuntimeException{
    public DuplicateDoctorException(String message){
        super(message);
    }
}
