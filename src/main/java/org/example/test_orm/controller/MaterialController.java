package org.example.test_orm.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.test_orm.entity.Material;
import org.example.test_orm.entity.Producer;
import org.example.test_orm.repository.MaterialsRepository;
import org.example.test_orm.repository.ProducerRepository;
import org.example.test_orm.service.MaterialService;
import org.example.test_orm.service.ProducerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Controller
@RequestMapping("/materials")
@RequiredArgsConstructor
public class MaterialController {
    private final MaterialService materialService;
    private final ProducerService producerService;
    private final ProducerRepository producerRepository;
    private final MaterialsRepository materialsRepository;

    @GetMapping
    public String getMaterials(Model model) {
        model.addAttribute("materials", materialService.getAllMaterials());
        model.addAttribute("producers", producerService.getAllProducers());
        return "inventory/materials";
    }

    @GetMapping("/create")
    public String createMaterialPage(Model model) {
        model.addAttribute("producers", producerService.getAllProducers());
        return "inventory/create";
    }

    @PostMapping("/create")
    public String createMaterial(@Valid Material material,
                                 BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/materials/create";
        }

        materialService.create(material);
        return "redirect:/materials";
    }

    @GetMapping("/search")
    public String search(@RequestParam(value = "word", required = false) String word,
                         Model model) {

        if (word != null) {
            model.addAttribute("materials", materialService.getMaterialsByName(word));
        } else {
            model.addAttribute("materials", materialService.getAllMaterials());
        }

        model.addAttribute("producers", producerService.getAllProducers());
        return "inventory/materials";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable("id") Long id,
                           Model model) {
        model.addAttribute("material", materialService.getMaterialById(id));
        model.addAttribute("producers", producerService.getAllProducers());
        return "inventory/material";
    }

    @PostMapping("/{id}")
    public String updateMaterial(@PathVariable("id") Long id,
                                 @Valid Material material,
                                 BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/materials/" + material.getId();
        }
        materialService.updateMaterial(id, material);
        return "redirect:/materials";
    }

    @PostMapping("/delete/{id}")
    public String deleteMaterial(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        if (materialService.deleteMaterialById(id)) {
            return "redirect:/materials";
        } else {
            redirectAttributes.addFlashAttribute("error", "Не удалось удалить материал с ID: " + id);
            return "redirect:/materials/" + id;
        }
    }

    @GetMapping("/refill/{id}")
    public String refillPage(@PathVariable("id") Long id,
                             Model model) {
        model.addAttribute("material", materialService.getMaterialById(id));
        return "inventory/refill";
    }

    @PostMapping("/refill/{id}")
    public String refill(@PathVariable("id") Long id,
                         Integer count,
                         RedirectAttributes redirectAttributes) {
        if (count == null || count < 0) {
            redirectAttributes.addFlashAttribute("errorMessage", "Количество должен быть положительным");
            return "redirect:/materials/refill/" + id;
        }
        if (materialService.updateCount(id, count)) {
            redirectAttributes.addFlashAttribute("message", "Успешно обновился");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Не удалось обновить");
        }
        return "redirect:/materials/refill/" + id;
    }

    @GetMapping("/producer/add")
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
            return "redirect:/materials/producer/delete";
        }

        String message = producerService.deleteProducer(Integer.valueOf(producerId))
                ? "Успешно удалился"
                : "Не удалось удалить";
        redirectAttributes.addFlashAttribute("message", message);
        return "redirect:/materials/producer/delete";
    }

    @PostMapping("/filter")
    public String filter(@RequestParam(value = "producerId", required = false) Integer producerId,
                         @RequestParam(value = "minPrice", required = false) BigDecimal minPrice,
                         @RequestParam(value = "maxPrice", required = false) BigDecimal maxPrice,
                         @RequestParam(value = "minCount", required = false) Integer minCount,
                         @RequestParam(value = "maxCount", required = false) Integer maxCount,
                         Model model) {

        List<Material> materials = materialService.getFilteredMaterials(producerId, minPrice, maxPrice, minCount, maxCount);

        model.addAttribute("materials", materials);
        model.addAttribute("producers", producerService.getAllProducers());

        return "inventory/materials";
    }

}