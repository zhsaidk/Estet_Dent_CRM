package org.example.test_orm.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.test_orm.entity.MedicalHistory;
import org.example.test_orm.service.AuthService;
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
    private final AuthService authService;

    @GetMapping("/create/{id}")
    public String createPage(@PathVariable("id") Long id,
                             Model model){
        model.addAttribute("doctors", authService.getAll());
        model.addAttribute("patientId", id);
        return "history/create";
    }

    @PostMapping("/create/{id}")
    public String create(@Valid MedicalHistory medicalHistory,
                         BindingResult bindingResult,
                         @PathVariable("id") Long id,
                         RedirectAttributes redirectAttributes,
                         Model model){
        model.addAttribute("patients", patientService.getPatients());
        model.addAttribute("doctors", authService.getAll());

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/history/create/" + id;
        }

        medicalHistoryService.create(medicalHistory);
        return "redirect:/patients/" + id;
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
