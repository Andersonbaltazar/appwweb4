package com.pcfinal.apiapp.service;

import com.pcfinal.apiapp.dto.ProductoCreateDTO;
import com.pcfinal.apiapp.dto.ProductoResponseDTO;
import com.pcfinal.apiapp.dto.ProductoUpdateDTO;

import java.math.BigDecimal;
import java.util.List;

public interface ProductoService {
    
    // Operaciones CRUD básicas
    ProductoResponseDTO crearProducto(ProductoCreateDTO productoCreateDTO);
    ProductoResponseDTO obtenerProductoPorId(Long id);
    List<ProductoResponseDTO> obtenerTodosLosProductos();
    ProductoResponseDTO actualizarProducto(Long id, ProductoUpdateDTO productoUpdateDTO);
    void eliminarProducto(Long id);
    
    // Métodos de búsqueda específicos
    List<ProductoResponseDTO> buscarProductosPorNombre(String nombre);
    List<ProductoResponseDTO> buscarProductosPorCategoria(Long categoriaId);
    List<ProductoResponseDTO> buscarProductosPorRangoPrecio(BigDecimal precioMin, BigDecimal precioMax);
    List<ProductoResponseDTO> obtenerProductosConStock();
    List<ProductoResponseDTO> obtenerProductosSinStock();
    List<ProductoResponseDTO> obtenerProductosConStockBajo(Integer stockMinimo);
    
    // Operaciones de negocio
    ProductoResponseDTO actualizarStock(Long id, Integer nuevoStock);
    boolean existeProductoConNombre(String nombre);
}
