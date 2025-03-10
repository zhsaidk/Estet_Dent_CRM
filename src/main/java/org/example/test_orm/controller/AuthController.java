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
    private final CookieService cookieService;

    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error, Model model) {
        if (error!=null){
            model.addAttribute("error", "Неверные учетные данные");
        }
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletResponse response) {
        cookieService.clearTokenCookie(response);
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
//
//        String errorMessage = doctorService.existsByEmail(doctor.getEmail()) ? "Пользователь с таким email уже существует"
//                : doctorService.existsByLogin(doctor.getLogin()) ? "Пользователь с таким логином уже существует"
//                : null;
//
//        if (errorMessage != null){
//            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
//            return "redirect:/register";
//        }
//
//        doctorService.save(doctor);
//        redirectAttributes.addFlashAttribute("message", "Registration successful, please sign in");
//        return "redirect:/login";
    }
}
