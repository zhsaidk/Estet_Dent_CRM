package org.example.test_orm.controller_advice;

import lombok.extern.slf4j.Slf4j;
import org.example.test_orm.entity.Patient;
import org.example.test_orm.exception.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@ControllerAdvice
@Slf4j
public class Advice {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public String errorMessageFromDataBase(DataIntegrityViolationException e, RedirectAttributes redirectAttributes) {
        log.warn(e.getClass().toString(), e);
        String errorMessage = e.getMessage();
        int start = errorMessage.indexOf("(") + 1;
        int last = errorMessage.indexOf(")");
        String showMessage = "Поле: " + errorMessage.substring(start, last) + " уже есть!";
        redirectAttributes.addFlashAttribute("error_message", showMessage);
        return "redirect:/patients/create";
    }


    @ExceptionHandler(value = {CreateDataOfBirthPatientException.class, TelephoneNumberException.class})
    public String errorMessageFromCreatePatient(PatientException e,  RedirectAttributes redirectAttributes) {
        log.warn(e.getClass().toString(), e);
        redirectAttributes.addFlashAttribute("error_message", e.getMessage()); // Подумать над этим позже
        return "redirect:/patients/create";
    }

    @ExceptionHandler(value = {PatientNotFoundException.class})
    public String errorMessageFromViewPatient(PatientException e, RedirectAttributes redirectAttributes) {
        log.warn(e.getClass().toString(), e);
        redirectAttributes.addFlashAttribute("error_message", e.getMessage());// Подумать над этим позже
        return "redirect:/patients";
    }

    @ExceptionHandler(DuplicateDoctorException.class)
    public String duplicateDoctor(DuplicateDoctorException e, RedirectAttributes redirectAttributes){
        log.warn(e.getClass().toString(), e);
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        return "redirect:/register";
    }

    @ExceptionHandler(DoctorSaveException.class)
    public String doctorSave(DoctorSaveException e, RedirectAttributes redirectAttributes){
        log.warn(e.getClass().toString(), e);
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        return "redirect:/register";
    }

}
