package org.example.test_orm.controller;

import jakarta.validation.Valid;
import org.example.test_orm.entity.Patient;
import org.example.test_orm.service.PatientService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/patients")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping
    public String getAllPatients(Model model) {
        model.addAttribute("patients", patientService.getPatients());
        return "index";
    }

    @GetMapping("/{id}")
    public String getPatient(Model model, @PathVariable long id) {
        model.addAttribute("patient", patientService.getPatient(id));
        return "patient";
    }

    @GetMapping("/create")
    public String createPatientPage(Model model) {
        model.addAttribute("patient", new Patient());
        return "create";
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute Patient patient, Model model) {
        patientService.createPatient(patient);
        model.addAttribute("patient", patient);
        return "redirect:/patients";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable long id) {
        patientService.deletePatient(id);
        return "index";
    }

}
