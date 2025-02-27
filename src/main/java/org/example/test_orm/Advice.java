package org.example.test_orm;

import org.example.test_orm.entity.Patient;
import org.example.test_orm.exception.TelephoneNumberException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class Advice {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public String qwe(DataIntegrityViolationException e, Model model) {
        String errorMessage = e.getMessage();
        int start = errorMessage.indexOf("(") + 1;
        int last = errorMessage.indexOf(")");
        String showMessage = "Поле: " + errorMessage.substring(start, last) + " уже есть!";
        model.addAttribute("error_message", showMessage);
        return "error_page";
    }

    @ExceptionHandler(TelephoneNumberException.class)
    public String qwe(TelephoneNumberException e, Model model) {

        model.addAttribute("error_message", e.getCause().getMessage()); // Подумать над этим позже
        model.addAttribute("patient", new Patient());
        return "create";
    }


}
