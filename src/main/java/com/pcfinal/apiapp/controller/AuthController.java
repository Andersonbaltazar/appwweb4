package com.pcfinal.apiapp.controller;

import com.pcfinal.apiapp.dto.JwtResponseDTO;
import com.pcfinal.apiapp.dto.LoginRequestDTO;
import com.pcfinal.apiapp.dto.MessageResponseDTO;
import com.pcfinal.apiapp.dto.SignupRequestDTO;
import com.pcfinal.apiapp.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    @Autowired
    AuthService authService;
    
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequestDTO loginRequest) {
        try {
            JwtResponseDTO response = authService.authenticateUser(loginRequest);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Credenciales inválidas");
            error.put("mensaje", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
    }
    
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequestDTO signUpRequest) {
        try {
            MessageResponseDTO response = authService.registerUser(signUpRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error interno del servidor");
            error.put("mensaje", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    @GetMapping("/check-username/{username}")
    public ResponseEntity<Map<String, Boolean>> checkUsername(@PathVariable String username) {
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", authService.existsByUsername(username));
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/check-email/{email}")
    public ResponseEntity<Map<String, Boolean>> checkEmail(@PathVariable String email) {
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", authService.existsByEmail(email));
        return ResponseEntity.ok(response);
    }
}
