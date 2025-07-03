package com.pcfinal.apiapp.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "roles")
public class Rol {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "name", length = 20)
    private ERole name;
    
    // Constructores
    public Rol() {}
    
    public Rol(ERole name) {
        this.name = name;
    }
    
    // Getters y Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public ERole getName() {
        return name;
    }
    
    public void setName(ERole name) {
        this.name = name;
    }
}
