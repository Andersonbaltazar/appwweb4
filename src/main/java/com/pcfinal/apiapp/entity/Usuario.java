package com.pcfinal.apiapp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "usuarios", 
       uniqueConstraints = {
           @UniqueConstraint(columnNames = "username"),
           @UniqueConstraint(columnNames = "email")
       })
public class Usuario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    @Size(max = 50)
    @Column(name = "username", nullable = false, unique = true)
    private String username;
    
    @NotBlank
    @Size(max = 100)
    @Email
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    
    @NotBlank
    @Size(max = 120)
    @Column(name = "password", nullable = false)
    private String password;
    
    @Column(name = "nombre")
    private String nombre;
    
    @Column(name = "apellido")
    private String apellido;
    
    @Column(name = "enabled")
    private Boolean enabled = true;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "usuario_roles",
               joinColumns = @JoinColumn(name = "usuario_id"),
               inverseJoinColumns = @JoinColumn(name = "rol_id"))
    private Set<Rol> roles = new HashSet<>();
    
    // Constructores
    public Usuario() {}
    
    public Usuario(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    public Usuario(String username, String email, String password, String nombre, String apellido) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.nombre = nombre;
        this.apellido = apellido;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    // Getters y Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
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
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
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
    
    public Boolean getEnabled() {
        return enabled;
    }
    
    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public Set<Rol> getRoles() {
        return roles;
    }
    
    public void setRoles(Set<Rol> roles) {
        this.roles = roles;
    }
}
