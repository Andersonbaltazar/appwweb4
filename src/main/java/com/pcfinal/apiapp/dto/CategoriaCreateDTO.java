package com.pcfinal.apiapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CategoriaCreateDTO {
    
    @NotBlank(message = "El nombre de la categoría es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    private String nombre;
    
    @Size(max = 500, message = "La descripción no puede exceder 500 caracteres")
    private String descripcion;
    
    // Constructores
    public CategoriaCreateDTO() {}
    
    public CategoriaCreateDTO(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }
    
    // Getters y Setters
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
