package com.pcfinal.apiapp.service;

import com.pcfinal.apiapp.dto.ProductoCreateDTO;
import com.pcfinal.apiapp.dto.ProductoResponseDTO;
import com.pcfinal.apiapp.dto.ProductoUpdateDTO;
import com.pcfinal.apiapp.dto.CategoriaResponseDTO;
import com.pcfinal.apiapp.entity.Producto;
import com.pcfinal.apiapp.entity.Categoria;
import com.pcfinal.apiapp.repository.ProductoRepository;
import com.pcfinal.apiapp.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductoServiceImpl implements ProductoService {
    
    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;
    
    @Autowired
    public ProductoServiceImpl(ProductoRepository productoRepository, CategoriaRepository categoriaRepository) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
    }
    
    @Override
    public ProductoResponseDTO crearProducto(ProductoCreateDTO productoCreateDTO) {
        // Verificar si ya existe un producto con el mismo nombre
        if (existeProductoConNombre(productoCreateDTO.getNombre())) {
            throw new RuntimeException("Ya existe un producto con el nombre: " + productoCreateDTO.getNombre());
        }
        
        // Verificar que la categoría existe
        Categoria categoria = categoriaRepository.findById(productoCreateDTO.getCategoriaId())
                .orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada con ID: " + productoCreateDTO.getCategoriaId()));
        
        Producto producto = convertirAEntity(productoCreateDTO, categoria);
        Producto productoGuardado = productoRepository.save(producto);
        return convertirAResponseDTO(productoGuardado);
    }
    
    @Override
    @Transactional(readOnly = true)
    public ProductoResponseDTO obtenerProductoPorId(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con ID: " + id));
        return convertirAResponseDTO(producto);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponseDTO> obtenerTodosLosProductos() {
        return productoRepository.findAll()
                .stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public ProductoResponseDTO actualizarProducto(Long id, ProductoUpdateDTO productoUpdateDTO) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con ID: " + id));
        
        // Actualizar solo los campos no nulos
        if (productoUpdateDTO.getNombre() != null && !productoUpdateDTO.getNombre().trim().isEmpty()) {
            // Verificar si el nuevo nombre ya existe en otro producto
            if (!producto.getNombre().equalsIgnoreCase(productoUpdateDTO.getNombre()) 
                && existeProductoConNombre(productoUpdateDTO.getNombre())) {
                throw new RuntimeException("Ya existe un producto con el nombre: " + productoUpdateDTO.getNombre());
            }
            producto.setNombre(productoUpdateDTO.getNombre());
        }
        
        if (productoUpdateDTO.getDescripcion() != null) {
            producto.setDescripcion(productoUpdateDTO.getDescripcion());
        }
        
        if (productoUpdateDTO.getPrecio() != null) {
            producto.setPrecio(productoUpdateDTO.getPrecio());
        }
        
        if (productoUpdateDTO.getStock() != null) {
            producto.setStock(productoUpdateDTO.getStock());
        }
        
        if (productoUpdateDTO.getCategoriaId() != null) {
            Categoria categoria = categoriaRepository.findById(productoUpdateDTO.getCategoriaId())
                    .orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada con ID: " + productoUpdateDTO.getCategoriaId()));
            producto.setCategoria(categoria);
        }
        
        Producto productoActualizado = productoRepository.save(producto);
        return convertirAResponseDTO(productoActualizado);
    }
    
    @Override
    public void eliminarProducto(Long id) {
        if (!productoRepository.existsById(id)) {
            throw new RuntimeException("Producto no encontrado con ID: " + id);
        }
        productoRepository.deleteById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponseDTO> buscarProductosPorNombre(String nombre) {
        return productoRepository.findByNombreContainingIgnoreCase(nombre)
                .stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponseDTO> buscarProductosPorCategoria(Long categoriaId) {
        return productoRepository.findByCategoriaId(categoriaId)
                .stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponseDTO> buscarProductosPorRangoPrecio(BigDecimal precioMin, BigDecimal precioMax) {
        return productoRepository.findByPrecioBetween(precioMin, precioMax)
                .stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponseDTO> obtenerProductosConStock() {
        return productoRepository.findByStockGreaterThan(0)
                .stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponseDTO> obtenerProductosSinStock() {
        return productoRepository.findByStock(0)
                .stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponseDTO> obtenerProductosConStockBajo(Integer stockMinimo) {
        return productoRepository.findProductosConStockBajo(stockMinimo)
                .stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public ProductoResponseDTO actualizarStock(Long id, Integer nuevoStock) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con ID: " + id));
        
        producto.setStock(nuevoStock);
        Producto productoActualizado = productoRepository.save(producto);
        return convertirAResponseDTO(productoActualizado);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existeProductoConNombre(String nombre) {
        return productoRepository.existsByNombreIgnoreCase(nombre);
    }
    
    // Métodos de conversión
    private ProductoResponseDTO convertirAResponseDTO(Producto producto) {
        // Convertir categoría a DTO
        CategoriaResponseDTO categoriaDTO = null;
        if (producto.getCategoria() != null) {
            categoriaDTO = new CategoriaResponseDTO(
                    producto.getCategoria().getId(),
                    producto.getCategoria().getNombre(),
                    producto.getCategoria().getDescripcion(),
                    producto.getCategoria().getActiva()
            );
        }
        
        return new ProductoResponseDTO(
                producto.getId(),
                producto.getNombre(),
                producto.getDescripcion(),
                producto.getPrecio(),
                producto.getStock(),
                categoriaDTO
        );
    }
    
    private Producto convertirAEntity(ProductoCreateDTO productoCreateDTO, Categoria categoria) {
        return new Producto(
                productoCreateDTO.getNombre(),
                productoCreateDTO.getDescripcion(),
                productoCreateDTO.getPrecio(),
                productoCreateDTO.getStock(),
                categoria
        );
    }
}
