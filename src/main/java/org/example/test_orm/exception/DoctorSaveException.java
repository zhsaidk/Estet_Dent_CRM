package org.example.test_orm.exception;

public class DoctorSaveException extends RuntimeException{
    public DoctorSaveException(String message){
        super(message);
    }
}
