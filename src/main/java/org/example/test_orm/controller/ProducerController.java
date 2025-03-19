package org.example.test_orm.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.test_orm.entity.Producer;
import org.example.test_orm.repository.ProducerRepository;
import org.example.test_orm.service.ProducerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ProducerController {
    private final ProducerService producerService;
    private final ProducerRepository producerRepository;
    @GetMapping("/producer")
    public String producerCreatePage() {
        return "inventory/producer";
    }

    @PostMapping("/producer/add")
    public String createProducer(@Valid Producer producer,
                                 BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/materials/producer/add";
        }
        Optional<Producer> prod = producerRepository.findProducerByName(producer.getName());
        if (prod.isPresent()){
            redirectAttributes.addFlashAttribute("errorMessage", "Производитель с таким названием уже существует");
            return "redirect:/materials/producer/add";
        }

        producerService.createProducer(producer);
        redirectAttributes.addFlashAttribute("message", "Производитель успешно добавился");
        return "redirect:/materials/create";
    }

    @GetMapping("/producer/delete")
    public String deleteProducerPage(Model model) {
        model.addAttribute("producers", producerService.getAllProducers());
        return "inventory/deleteProducerPage";
    }

    @PostMapping("/producer/delete")
    public String deleteProducerPage(@RequestParam("producerId") String producerId,
                                     RedirectAttributes redirectAttributes) {
        if (producerId == null || producerId.isBlank()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Выберите производителя перед удалением!");
            return "redirect:/producer/delete";
        }

        String message = producerService.deleteProducer(Integer.valueOf(producerId))
                ? "Успешно удалился"
                : "Не удалось удалить";
        redirectAttributes.addFlashAttribute("message", message);
        return "redirect:/producer/delete";
    }

}
