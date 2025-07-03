package com.pcfinal.apiapp.dto.web;

import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO para el formulario de login web")
public class LoginWebDTO {
    
    @Schema(description = "Nombre de usuario", example = "admin")
    @NotBlank(message = "El nombre de usuario es obligatorio")
    private String username;
    
    @Schema(description = "Contraseña del usuario")
    @NotBlank(message = "La contraseña es obligatoria")
    private String password;
    
    private boolean rememberMe;
    
    // Constructores
    public LoginWebDTO() {}
    
    public LoginWebDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }
    
    // Getters y Setters
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public boolean isRememberMe() {
        return rememberMe;
    }
    
    public void setRememberMe(boolean rememberMe) {
        this.rememberMe = rememberMe;
    }
}
