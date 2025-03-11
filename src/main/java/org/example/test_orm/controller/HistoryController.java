package org.example.test_orm.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.test_orm.entity.MedicalHistory;
import org.example.test_orm.service.DoctorService;
import org.example.test_orm.service.MedicalHistoryService;
import org.example.test_orm.service.PatientService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/history")
@RequiredArgsConstructor
public class HistoryController {
    private final PatientService patientService;;
    private final MedicalHistoryService medicalHistoryService;
    private final DoctorService doctorService;

    @GetMapping("/{id}")
    public String getHistoryPage(@PathVariable("id") Long id,
                                 Model model) {
        model.addAttribute("patient", patientService.getPatient(id));
        model.addAttribute("histories", medicalHistoryService.getAllByClientId(id));
        return "history/history";
    }

    @GetMapping("/create/{id}")
    public String createPage(@PathVariable("id") Long id,
                             Model model){
        model.addAttribute("patients", patientService.getPatients());
        model.addAttribute("doctors", doctorService.getAll());
        model.addAttribute("currentPatientId", id);
        return "history/create";
    }

    @PostMapping("/create")
    public String create(@Valid MedicalHistory medicalHistory,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes,
                         Model model){
        model.addAttribute("patients", patientService.getPatients());
        model.addAttribute("doctors", doctorService.getAll());

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/history/create/" + medicalHistory.getMedicalHistoryID();
        }

        medicalHistoryService.create(medicalHistory);
        return "redirect:/history/" + medicalHistory.getMedicalHistoryOfClients().getID();
    }

    @PostMapping("/delete/{id}")
    public String deleteHisotry(@PathVariable("id") Long id,
                                RedirectAttributes redirectAttributes){
        if (medicalHistoryService.deleteById(id)) {
            redirectAttributes.addFlashAttribute("message", "Успешно удалился");
        }
        else{
            redirectAttributes.addFlashAttribute("message", "Не удалось удалить");
        }
        return "redirect:/history/" + id;
    }
}
