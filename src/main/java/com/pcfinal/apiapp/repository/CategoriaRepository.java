package com.pcfinal.apiapp.repository;

import com.pcfinal.apiapp.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    
    // Buscar categorías activas
    List<Categoria> findByActivaTrue();
    
    // Buscar categoría por nombre (case insensitive)
    Optional<Categoria> findByNombreIgnoreCase(String nombre);
    
    // Verificar si existe una categoría con un nombre específico
    boolean existsByNombreIgnoreCase(String nombre);
    
    // Buscar categorías por nombre que contenga un texto
    List<Categoria> findByNombreContainingIgnoreCase(String nombre);
}
