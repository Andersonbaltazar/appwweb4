package com.pcfinal.apiapp.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(description = "DTO de respuesta con información del producto")
public class ProductoResponseDTO {
    
    @Schema(description = "ID único del producto", example = "1")
    private Long id;
    
    @Schema(description = "Nombre del producto", example = "Laptop Gaming")
    private String nombre;
    
    @Schema(description = "Descripción del producto", example = "Laptop para gaming de alta gama")
    private String descripcion;
    
    @Schema(description = "Precio del producto", example = "1999.99")
    private BigDecimal precio;
    
    @Schema(description = "Cantidad en stock", example = "10")
    private Integer stock;
    
    @Schema(description = "Información de la categoría")
    private CategoriaResponseDTO categoria;
    
    // Constructores
    public ProductoResponseDTO() {}
    
    public ProductoResponseDTO(Long id, String nombre, String descripcion, BigDecimal precio, Integer stock, CategoriaResponseDTO categoria) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;
        this.categoria = categoria;
    }
    
    // Getters y Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
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
    
    public CategoriaResponseDTO getCategoria() {
        return categoria;
    }
    
    public void setCategoria(CategoriaResponseDTO categoria) {
        this.categoria = categoria;
    }
}
