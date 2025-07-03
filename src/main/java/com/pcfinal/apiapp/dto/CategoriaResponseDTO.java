package com.pcfinal.apiapp.dto;

public class CategoriaResponseDTO {
    
    private Long id;
    private String nombre;
    private String descripcion;
    private Boolean activa;
    private Integer cantidadProductos;
    
    // Constructores
    public CategoriaResponseDTO() {}
    
    public CategoriaResponseDTO(Long id, String nombre, String descripcion, Boolean activa) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.activa = activa;
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
    
    public Boolean getActiva() {
        return activa;
    }
    
    public void setActiva(Boolean activa) {
        this.activa = activa;
    }
    
    public Integer getCantidadProductos() {
        return cantidadProductos;
    }
    
    public void setCantidadProductos(Integer cantidadProductos) {
        this.cantidadProductos = cantidadProductos;
    }
}
