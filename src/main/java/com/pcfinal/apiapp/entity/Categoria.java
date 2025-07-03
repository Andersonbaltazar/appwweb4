package com.pcfinal.apiapp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.List;

@Entity
@Table(name = "categorias")
public class Categoria {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "nombre", nullable = false, length = 100, unique = true)
    @NotBlank(message = "El nombre de la categoría es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    private String nombre;
    
    @Column(name = "descripcion", length = 500)
    @Size(max = 500, message = "La descripción no puede exceder 500 caracteres")
    private String descripcion;
    
    @Column(name = "activa", nullable = false)
    private Boolean activa = true;
    
    @OneToMany(mappedBy = "categoria", fetch = FetchType.LAZY)
    private List<Producto> productos;
    
    // Constructores
    public Categoria() {}
    
    public Categoria(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.activa = true;
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
    
    public List<Producto> getProductos() {
        return productos;
    }
    
    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }
    
    @Override
    public String toString() {
        return "Categoria{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", activa=" + activa +
                '}';
    }
}
