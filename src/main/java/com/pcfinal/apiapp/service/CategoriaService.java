package com.pcfinal.apiapp.service;

import com.pcfinal.apiapp.dto.CategoriaCreateDTO;
import com.pcfinal.apiapp.dto.CategoriaResponseDTO;
import java.util.List;

public interface CategoriaService {
    
    // Operaciones CRUD básicas
    CategoriaResponseDTO crearCategoria(CategoriaCreateDTO categoriaCreateDTO);
    CategoriaResponseDTO obtenerCategoriaPorId(Long id);
    List<CategoriaResponseDTO> obtenerTodasLasCategorias();
    List<CategoriaResponseDTO> obtenerCategoriasActivas();
    CategoriaResponseDTO actualizarCategoria(Long id, CategoriaCreateDTO categoriaUpdateDTO);
    void eliminarCategoria(Long id);
    
    // Operaciones de negocio
    void activarCategoria(Long id);
    void desactivarCategoria(Long id);
    boolean existeCategoriaConNombre(String nombre);
    List<CategoriaResponseDTO> buscarCategoriasPorNombre(String nombre);
}
