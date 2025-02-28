package org.example.test_orm.controller_advice;

import lombok.extern.slf4j.Slf4j;
import org.example.test_orm.entity.Patient;
import org.example.test_orm.exception.CreateDataOfBirthPatient;
import org.example.test_orm.exception.CreatePatientException;
import org.example.test_orm.exception.TelephoneNumberException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class Advice {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public String errorMessageFromDataBase(DataIntegrityViolationException e, Model model) {
        log.warn(e.getClass().toString(), e);
        String errorMessage = e.getMessage();
        int start = errorMessage.indexOf("(") + 1;
        int last = errorMessage.indexOf(")");
        String showMessage = "Поле: " + errorMessage.substring(start, last) + " уже есть!";
        model.addAttribute("error_message", showMessage);
        model.addAttribute("patient", new Patient());
        return "create";
    }


    @ExceptionHandler(value = {CreateDataOfBirthPatient.class, TelephoneNumberException.class})
    public String errorMessageFromCreatePatient(CreatePatientException e, Model model) {
        log.warn(e.getClass().toString(), e);
        model.addAttribute("error_message", e.getMessage()); // Подумать над этим позже
        model.addAttribute("patient", new Patient());
        return "create";
    }





}
