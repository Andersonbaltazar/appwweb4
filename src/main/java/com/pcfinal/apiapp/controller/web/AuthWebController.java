package com.pcfinal.apiapp.controller.web;

import com.pcfinal.apiapp.dto.SignupRequestDTO;
import com.pcfinal.apiapp.dto.web.LoginWebDTO;
import com.pcfinal.apiapp.dto.web.RegisterWebDTO;
import com.pcfinal.apiapp.service.AuthService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/web")
public class AuthWebController {
    
    @Autowired
    private AuthService authService;
    
    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("loginForm", new LoginWebDTO());
        model.addAttribute("titulo", "Iniciar Sesión");
        return "auth/login";
    }
    
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("registerForm", new RegisterWebDTO());
        model.addAttribute("titulo", "Registro de Usuario");
        return "auth/register";
    }
    
    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("registerForm") RegisterWebDTO registerForm, 
                               BindingResult result, 
                               Model model, 
                               RedirectAttributes redirectAttributes) {
        
        model.addAttribute("titulo", "Registro de Usuario");
        
        // Validar que las contraseñas coincidan
        if (!registerForm.getPassword().equals(registerForm.getConfirmPassword())) {
            result.rejectValue("confirmPassword", "error.confirmPassword", "Las contraseñas no coinciden");
        }
        
        // Verificar si el nombre de usuario ya existe
        if (authService.existsByUsername(registerForm.getUsername())) {
            result.rejectValue("username", "error.username", "El nombre de usuario ya está en uso");
        }
        
        // Verificar si el email ya existe
        if (authService.existsByEmail(registerForm.getEmail())) {
            result.rejectValue("email", "error.email", "El email ya está registrado");
        }
        
        if (result.hasErrors()) {
            return "auth/register";
        }
        
        // Convertir DTO web a DTO de API
        SignupRequestDTO signupRequest = new SignupRequestDTO(
                registerForm.getUsername(),
                registerForm.getEmail(),
                registerForm.getPassword()
        );
        signupRequest.setNombre(registerForm.getNombre());
        signupRequest.setApellido(registerForm.getApellido());
        
        try {
            authService.registerUser(signupRequest);
            redirectAttributes.addFlashAttribute("successMessage", "Cuenta creada exitosamente. Ahora puedes iniciar sesión.");
            return "redirect:/web/login";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error al registrar: " + e.getMessage());
            return "auth/register";
        }
    }
    
    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        // Asegurarnos de que estamos pasando los datos correctos al modelo
        if (auth != null && auth.getPrincipal() != null) {
            model.addAttribute("usuario", auth.getPrincipal());
            model.addAttribute("titulo", "Dashboard");
            return "dashboard/index";
        } else {
            return "redirect:/web/login";
        }
    }
}
