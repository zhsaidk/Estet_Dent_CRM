package org.example.test_orm.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.test_orm.entity.Doctor;
import org.example.test_orm.entity.Token;
import org.example.test_orm.repository.DoctorRepository;
import org.example.test_orm.service.AuthService;
import org.example.test_orm.service.CookieService;
import org.example.test_orm.service.TokenService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error_message", required = false) String error, Model model) {
        if (error!=null){
            model.addAttribute("error_message", "Неверные учетные данные");
        }
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletResponse response) {
        authService.clearTokenCookie(response);
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "registration";
    }

    @PostMapping("/register")
    public String registration(@Valid Doctor doctor,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {
        if (!bindingResult.hasErrors()) {
            authService.save(doctor);
            return "redirect:/login";
        }   else {
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/register";
        }


    }
}
