package com.pcfinal.apiapp.dto;

import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(description = "DTO para actualizar un producto existente")
public class ProductoUpdateDTO {
    
    @Schema(description = "Nuevo nombre del producto", example = "Laptop Gaming Pro")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    private String nombre;
    
    @Schema(description = "Nueva descripción del producto", example = "Laptop para gaming profesional con RTX 4080")
    @Size(max = 500, message = "La descripción no puede exceder 500 caracteres")
    private String descripcion;
    
    @Schema(description = "Nuevo precio del producto", example = "2299.99")
    @DecimalMin(value = "0.01", message = "El precio debe ser mayor a 0")
    private BigDecimal precio;
    
    @Schema(description = "Nueva cantidad en stock", example = "15")
    @Min(value = 0, message = "El stock no puede ser negativo")
    private Integer stock;
    
    @Schema(description = "Nueva categoría del producto", example = "2")
    private Long categoriaId;
    
    // Constructores
    public ProductoUpdateDTO() {}
    
    public ProductoUpdateDTO(String nombre, String descripcion, BigDecimal precio, Integer stock, Long categoriaId) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;
        this.categoriaId = categoriaId;
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
    
    public BigDecimal getPrecio() {
        return precio;
    }
    
    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }
    
    public Integer getStock() {
        return stock;
    }
    
    public void setStock(Integer stock) {
        this.stock = stock;
    }
    
    public Long getCategoriaId() {
        return categoriaId;
    }
    
    public void setCategoriaId(Long categoriaId) {
        this.categoriaId = categoriaId;
    }
}
