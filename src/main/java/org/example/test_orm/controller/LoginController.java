package org.example.test_orm.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.test_orm.config.PasswordEncoderConfig;
import org.example.test_orm.entity.Doctor;
import org.example.test_orm.repository.DoctorRepository;
import org.example.test_orm.service.DoctorService;
import org.example.test_orm.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class LoginController {
    private final DoctorService doctorService;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                        Model model) {
        if (error!=null){
            model.addAttribute("error", "Неверные учетные данные");
        }
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletResponse response) {
        tokenService.clearAuthCookies(response);
        SecurityContextHolder.clearContext();
        return "redirect:/login";
    }

    @GetMapping("/register")
    public String register() {
        return "registration";
    }

    @PostMapping("/register")
    public String registration(@Valid Doctor doctor,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/register";
        }

        String errorMessage = doctorService.existsByEmail(doctor.getEmail()) ? "Пользователь с таким email уже существует"
                : doctorService.existsByLogin(doctor.getLogin()) ? "Пользователь с таким логином уже существует"
                : null;

        if (errorMessage != null){
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            return "redirect:/register";
        }

        doctorService.save(doctor);
        redirectAttributes.addFlashAttribute("message", "Registration successful, please sign in");
        return "redirect:/login";
    }
}
