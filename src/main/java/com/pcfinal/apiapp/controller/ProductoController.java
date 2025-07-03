package com.pcfinal.apiapp.controller;

import com.pcfinal.apiapp.dto.ProductoCreateDTO;
import com.pcfinal.apiapp.dto.ProductoResponseDTO;
import com.pcfinal.apiapp.dto.ProductoUpdateDTO;
import com.pcfinal.apiapp.service.ProductoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "*")
@Tag(name = "Productos", description = "API para la gestión de productos")
@SecurityRequirement(name = "bearerAuth")
public class ProductoController {
    
    private final ProductoService productoService;
    
    @Autowired
    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }
    
    // GET - Obtener todos los productos
    @Operation(
        summary = "Obtener todos los productos",
        description = "Recupera una lista de todos los productos disponibles. Requiere autenticación JWT."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de productos obtenida exitosamente",
                    content = @Content(mediaType = "application/json", 
                    schema = @Schema(implementation = ProductoResponseDTO.class))),
        @ApiResponse(responseCode = "401", description = "No autorizado - Token JWT requerido"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<List<ProductoResponseDTO>> obtenerTodosLosProductos() {
        try {
            List<ProductoResponseDTO> productos = productoService.obtenerTodosLosProductos();
            return ResponseEntity.ok(productos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // GET - Obtener producto por ID
    @Operation(
        summary = "Obtener producto por ID",
        description = "Recupera un producto específico por su ID. Requiere autenticación JWT."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Producto encontrado",
                    content = @Content(mediaType = "application/json", 
                    schema = @Schema(implementation = ProductoResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
        @ApiResponse(responseCode = "401", description = "No autorizado - Token JWT requerido"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponseDTO> obtenerProductoPorId(
            @Parameter(description = "ID del producto a buscar", required = true)
            @PathVariable Long id) {
        try {
            ProductoResponseDTO producto = productoService.obtenerProductoPorId(id);
            return ResponseEntity.ok(producto);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // POST - Crear nuevo producto (solo ADMIN y MODERATOR)
    @Operation(
        summary = "Crear nuevo producto",
        description = "Crea un nuevo producto en el sistema. Requiere rol de ADMIN o MODERATOR."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Producto creado exitosamente",
                    content = @Content(mediaType = "application/json", 
                    schema = @Schema(implementation = ProductoResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "401", description = "No autorizado - Token JWT requerido"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado - Requiere rol ADMIN o MODERATOR"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
    public ResponseEntity<?> crearProducto(
            @Parameter(description = "Datos del producto a crear", required = true)
            @Valid @RequestBody ProductoCreateDTO productoCreateDTO) {
        try {
            ProductoResponseDTO producto = productoService.crearProducto(productoCreateDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(producto);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error interno del servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    // PUT - Actualizar producto completo (solo ADMIN y MODERATOR)
    @Operation(
        summary = "Actualizar producto completo",
        description = "Actualiza todos los campos de un producto existente. Requiere rol de ADMIN o MODERATOR."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Producto actualizado exitosamente",
                    content = @Content(mediaType = "application/json", 
                    schema = @Schema(implementation = ProductoResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
        @ApiResponse(responseCode = "401", description = "No autorizado - Token JWT requerido"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado - Requiere rol ADMIN o MODERATOR"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
    public ResponseEntity<?> actualizarProducto(
            @Parameter(description = "ID del producto a actualizar", required = true)
            @PathVariable Long id, 
            @Parameter(description = "Nuevos datos del producto", required = true)
            @Valid @RequestBody ProductoUpdateDTO productoUpdateDTO) {
        try {
            ProductoResponseDTO producto = productoService.actualizarProducto(id, productoUpdateDTO);
            return ResponseEntity.ok(producto);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error interno del servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    // DELETE - Eliminar producto (solo ADMIN)
    @Operation(
        summary = "Eliminar producto",
        description = "Elimina un producto del sistema. Requiere rol de ADMIN."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Producto eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
        @ApiResponse(responseCode = "401", description = "No autorizado - Token JWT requerido"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado - Requiere rol ADMIN"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> eliminarProducto(
            @Parameter(description = "ID del producto a eliminar", required = true)
            @PathVariable Long id) {
        try {
            productoService.eliminarProducto(id);
            Map<String, String> respuesta = new HashMap<>();
            respuesta.put("mensaje", "Producto eliminado exitosamente");
            return ResponseEntity.ok(respuesta);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error interno del servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    // GET - Buscar productos por nombre
    @Operation(
        summary = "Buscar productos por nombre",
        description = "Busca productos que contengan el nombre especificado. Requiere autenticación JWT."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Búsqueda realizada exitosamente",
                    content = @Content(mediaType = "application/json", 
                    schema = @Schema(implementation = ProductoResponseDTO.class))),
        @ApiResponse(responseCode = "401", description = "No autorizado - Token JWT requerido"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/buscar")
    public ResponseEntity<List<ProductoResponseDTO>> buscarProductosPorNombre(
            @Parameter(description = "Nombre o parte del nombre del producto a buscar", required = true)
            @RequestParam String nombre) {
        try {
            List<ProductoResponseDTO> productos = productoService.buscarProductosPorNombre(nombre);
            return ResponseEntity.ok(productos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // GET - Buscar productos por rango de precio
    @Operation(
        summary = "Buscar productos por rango de precio",
        description = "Busca productos dentro de un rango de precios específico. Requiere autenticación JWT."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Búsqueda por rango de precio realizada exitosamente",
                    content = @Content(mediaType = "application/json", 
                    schema = @Schema(implementation = ProductoResponseDTO.class))),
        @ApiResponse(responseCode = "401", description = "No autorizado - Token JWT requerido"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/rango-precio")
    public ResponseEntity<List<ProductoResponseDTO>> buscarProductosPorRangoPrecio(
            @Parameter(description = "Precio mínimo", required = true)
            @RequestParam BigDecimal precioMin, 
            @Parameter(description = "Precio máximo", required = true)
            @RequestParam BigDecimal precioMax) {
        try {
            List<ProductoResponseDTO> productos = productoService.buscarProductosPorRangoPrecio(precioMin, precioMax);
            return ResponseEntity.ok(productos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // GET - Obtener productos con stock
    @GetMapping("/con-stock")
    public ResponseEntity<List<ProductoResponseDTO>> obtenerProductosConStock() {
        try {
            List<ProductoResponseDTO> productos = productoService.obtenerProductosConStock();
            return ResponseEntity.ok(productos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // GET - Obtener productos sin stock
    @GetMapping("/sin-stock")
    public ResponseEntity<List<ProductoResponseDTO>> obtenerProductosSinStock() {
        try {
            List<ProductoResponseDTO> productos = productoService.obtenerProductosSinStock();
            return ResponseEntity.ok(productos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // GET - Obtener productos con stock bajo
    @GetMapping("/stock-bajo")
    public ResponseEntity<List<ProductoResponseDTO>> obtenerProductosConStockBajo(@RequestParam Integer stockMinimo) {
        try {
            List<ProductoResponseDTO> productos = productoService.obtenerProductosConStockBajo(stockMinimo);
            return ResponseEntity.ok(productos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // GET - Obtener productos por categoría
    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity<List<ProductoResponseDTO>> obtenerProductosPorCategoria(
            @Parameter(description = "ID de la categoría", required = true)
            @PathVariable Long categoriaId) {
        try {
            List<ProductoResponseDTO> productos = productoService.buscarProductosPorCategoria(categoriaId);
            return ResponseEntity.ok(productos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // PATCH - Actualizar solo el stock (solo ADMIN y MODERATOR)
    @Operation(
        summary = "Actualizar stock del producto",
        description = "Actualiza únicamente el stock de un producto existente. Requiere rol de ADMIN o MODERATOR."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Stock actualizado exitosamente",
                    content = @Content(mediaType = "application/json", 
                    schema = @Schema(implementation = ProductoResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Valor de stock inválido"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
        @ApiResponse(responseCode = "401", description = "No autorizado - Token JWT requerido"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado - Requiere rol ADMIN o MODERATOR"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PatchMapping("/{id}/stock")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
    public ResponseEntity<?> actualizarStock(
            @Parameter(description = "ID del producto", required = true)
            @PathVariable Long id, 
            @Parameter(description = "Nuevo valor de stock", required = true)
            @RequestParam Integer nuevoStock) {
        try {
            ProductoResponseDTO producto = productoService.actualizarStock(id, nuevoStock);
            return ResponseEntity.ok(producto);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error interno del servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    // GET - Verificar si existe producto con nombre
    @GetMapping("/existe")
    public ResponseEntity<Map<String, Boolean>> existeProductoConNombre(@RequestParam String nombre) {
        try {
            boolean existe = productoService.existeProductoConNombre(nombre);
            Map<String, Boolean> respuesta = new HashMap<>();
            respuesta.put("existe", existe);
            return ResponseEntity.ok(respuesta);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
