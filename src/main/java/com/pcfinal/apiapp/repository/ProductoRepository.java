package com.pcfinal.apiapp.repository;

import com.pcfinal.apiapp.entity.Producto;
import com.pcfinal.apiapp.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    
    // Buscar productos por nombre (case insensitive)
    List<Producto> findByNombreContainingIgnoreCase(String nombre);
    
    // Buscar productos por categoría
    List<Producto> findByCategoria(Categoria categoria);
    
    // Buscar productos por ID de categoría
    List<Producto> findByCategoriaId(Long categoriaId);
    
    // Buscar productos por rango de precio
    List<Producto> findByPrecioBetween(BigDecimal precioMin, BigDecimal precioMax);
    
    // Buscar productos con stock mayor a un valor
    List<Producto> findByStockGreaterThan(Integer stock);
    
    // Buscar productos sin stock
    List<Producto> findByStock(Integer stock);
    
    // Verificar si existe un producto con un nombre específico
    boolean existsByNombreIgnoreCase(String nombre);
    
    // Buscar producto por nombre exacto (case insensitive)
    Optional<Producto> findByNombreIgnoreCase(String nombre);
    
    // Query personalizada para buscar productos con stock bajo
    @Query("SELECT p FROM Producto p WHERE p.stock <= :stockMinimo")
    List<Producto> findProductosConStockBajo(@Param("stockMinimo") Integer stockMinimo);
    
    // Query personalizada para contar productos por categoría
    @Query("SELECT COUNT(p) FROM Producto p WHERE p.categoria.id = :categoriaId")
    Long countByCategoriaId(@Param("categoriaId") Long categoriaId);
}
