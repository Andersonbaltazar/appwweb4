package com.pcfinal.apiapp.dto.web;

import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO para el formulario de registro web")
public class RegisterWebDTO {
    
    @Schema(description = "Nombre de usuario", example = "usuario123")
    @NotBlank(message = "El nombre de usuario es obligatorio")
    @Size(min = 3, max = 20, message = "El nombre de usuario debe tener entre 3 y 20 caracteres")
    private String username;
    
    @Schema(description = "Correo electrónico", example = "usuario@example.com")
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Debe ser un email válido")
    private String email;
    
    @Schema(description = "Nombre del usuario", example = "Juan")
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 50, message = "El nombre no puede exceder 50 caracteres")
    private String nombre;
    
    @Schema(description = "Apellido del usuario", example = "Pérez")
    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 50, message = "El apellido no puede exceder 50 caracteres")
    private String apellido;
    
    @Schema(description = "Contraseña")
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;
    
    @Schema(description = "Confirmación de contraseña")
    @NotBlank(message = "Debe confirmar la contraseña")
    private String confirmPassword;
    
    // Constructores
    public RegisterWebDTO() {}
    
    // Getters y Setters
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getApellido() {
        return apellido;
    }
    
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getConfirmPassword() {
        return confirmPassword;
    }
    
    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
    
    // Método de validación personalizada
    public boolean isPasswordMatching() {
        return password != null && password.equals(confirmPassword);
    }
}
