package com.pcfinal.apiapp.service;

import com.pcfinal.apiapp.dto.CategoriaCreateDTO;
import com.pcfinal.apiapp.dto.CategoriaResponseDTO;
import com.pcfinal.apiapp.entity.Categoria;
import com.pcfinal.apiapp.repository.CategoriaRepository;
import com.pcfinal.apiapp.repository.ProductoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CategoriaServiceImpl implements CategoriaService {
    
    private final CategoriaRepository categoriaRepository;
    private final ProductoRepository productoRepository;
    
    @Autowired
    public CategoriaServiceImpl(CategoriaRepository categoriaRepository, ProductoRepository productoRepository) {
        this.categoriaRepository = categoriaRepository;
        this.productoRepository = productoRepository;
    }
    
    @Override
    public CategoriaResponseDTO crearCategoria(CategoriaCreateDTO categoriaCreateDTO) {
        // Verificar si ya existe una categoría con el mismo nombre
        if (existeCategoriaConNombre(categoriaCreateDTO.getNombre())) {
            throw new RuntimeException("Ya existe una categoría con el nombre: " + categoriaCreateDTO.getNombre());
        }
        
        Categoria categoria = convertirAEntity(categoriaCreateDTO);
        Categoria categoriaGuardada = categoriaRepository.save(categoria);
        return convertirAResponseDTO(categoriaGuardada);
    }
    
    @Override
    @Transactional(readOnly = true)
    public CategoriaResponseDTO obtenerCategoriaPorId(Long id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada con ID: " + id));
        return convertirAResponseDTO(categoria);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<CategoriaResponseDTO> obtenerTodasLasCategorias() {
        return categoriaRepository.findAll()
                .stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<CategoriaResponseDTO> obtenerCategoriasActivas() {
        return categoriaRepository.findByActivaTrue()
                .stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public CategoriaResponseDTO actualizarCategoria(Long id, CategoriaCreateDTO categoriaUpdateDTO) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada con ID: " + id));
        
        // Verificar si el nuevo nombre ya existe en otra categoría
        if (categoriaUpdateDTO.getNombre() != null && 
            !categoria.getNombre().equalsIgnoreCase(categoriaUpdateDTO.getNombre()) && 
            existeCategoriaConNombre(categoriaUpdateDTO.getNombre())) {
            throw new RuntimeException("Ya existe una categoría con el nombre: " + categoriaUpdateDTO.getNombre());
        }
        
        if (categoriaUpdateDTO.getNombre() != null && !categoriaUpdateDTO.getNombre().trim().isEmpty()) {
            categoria.setNombre(categoriaUpdateDTO.getNombre());
        }
        
        if (categoriaUpdateDTO.getDescripcion() != null) {
            categoria.setDescripcion(categoriaUpdateDTO.getDescripcion());
        }
        
        Categoria categoriaActualizada = categoriaRepository.save(categoria);
        return convertirAResponseDTO(categoriaActualizada);
    }
    
    @Override
    public void eliminarCategoria(Long id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada con ID: " + id));
        
        // Verificar si tiene productos asociados
        Long cantidadProductos = productoRepository.countByCategoriaId(id);
        if (cantidadProductos > 0) {
            throw new RuntimeException("No se puede eliminar la categoría porque tiene " + cantidadProductos + " productos asociados");
        }
        
        categoriaRepository.delete(categoria);
    }
    
    @Override
    public void activarCategoria(Long id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada con ID: " + id));
        categoria.setActiva(true);
        categoriaRepository.save(categoria);
    }
    
    @Override
    public void desactivarCategoria(Long id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada con ID: " + id));
        categoria.setActiva(false);
        categoriaRepository.save(categoria);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existeCategoriaConNombre(String nombre) {
        return categoriaRepository.existsByNombreIgnoreCase(nombre);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<CategoriaResponseDTO> buscarCategoriasPorNombre(String nombre) {
        return categoriaRepository.findByNombreContainingIgnoreCase(nombre)
                .stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }
    
    // Métodos de conversión
    private Categoria convertirAEntity(CategoriaCreateDTO dto) {
        return new Categoria(dto.getNombre(), dto.getDescripcion());
    }
    
    private CategoriaResponseDTO convertirAResponseDTO(Categoria categoria) {
        CategoriaResponseDTO dto = new CategoriaResponseDTO(
                categoria.getId(),
                categoria.getNombre(),
                categoria.getDescripcion(),
                categoria.getActiva()
        );
        
        // Contar productos asociados
        Long cantidadProductos = productoRepository.countByCategoriaId(categoria.getId());
        dto.setCantidadProductos(cantidadProductos.intValue());
        
        return dto;
    }
}
