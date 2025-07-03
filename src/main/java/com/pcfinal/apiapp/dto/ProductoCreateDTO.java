package com.pcfinal.apiapp.dto;

import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(description = "DTO para crear un nuevo producto")
public class ProductoCreateDTO {
    
    @Schema(description = "Nombre del producto", example = "Laptop Gaming", required = true)
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    private String nombre;
    
    @Schema(description = "Descripción detallada del producto", example = "Laptop para gaming de alta gama con tarjeta gráfica dedicada")
    @Size(max = 500, message = "La descripción no puede exceder 500 caracteres")
    private String descripcion;
    
    @Schema(description = "Precio del producto en formato decimal", example = "1999.99", required = true)
    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio debe ser mayor a 0")
    private BigDecimal precio;
    
    @Schema(description = "Cantidad en stock del producto", example = "10", required = true)
    @NotNull(message = "El stock es obligatorio")
    @Min(value = 0, message = "El stock no puede ser negativo")
    private Integer stock;
    
    @Schema(description = "ID de la categoría del producto", example = "1", required = true)
    @NotNull(message = "La categoría es obligatoria")
    private Long categoriaId;
    
    // Constructores
    public ProductoCreateDTO() {}
    
    public ProductoCreateDTO(String nombre, String descripcion, BigDecimal precio, Integer stock, Long categoriaId) {
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
