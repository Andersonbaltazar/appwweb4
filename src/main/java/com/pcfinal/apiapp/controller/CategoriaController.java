package com.pcfinal.apiapp.controller;

import com.pcfinal.apiapp.dto.CategoriaCreateDTO;
import com.pcfinal.apiapp.dto.CategoriaResponseDTO;
import com.pcfinal.apiapp.service.CategoriaService;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/categorias")
@CrossOrigin(origins = "*")
@Tag(name = "Categorías", description = "API para la gestión de categorías de productos")
@SecurityRequirement(name = "bearerAuth")
public class CategoriaController {
    
    @Autowired
    private CategoriaService categoriaService;
    
    // GET - Obtener todas las categorías
    @Operation(
        summary = "Obtener todas las categorías",
        description = "Devuelve una lista de todas las categorías registradas en el sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de categorías obtenida exitosamente",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CategoriaResponseDTO.class))),
        @ApiResponse(responseCode = "401", description = "No autorizado - Token JWT requerido")
    })
    @GetMapping
    public ResponseEntity<List<CategoriaResponseDTO>> obtenerTodasLasCategorias() {
        try {
            List<CategoriaResponseDTO> categorias = categoriaService.obtenerTodasLasCategorias();
            return ResponseEntity.ok(categorias);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // GET - Obtener categorías activas
    @GetMapping("/activas")
    public ResponseEntity<List<CategoriaResponseDTO>> obtenerCategoriasActivas() {
        try {
            List<CategoriaResponseDTO> categorias = categoriaService.obtenerCategoriasActivas();
            return ResponseEntity.ok(categorias);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // GET - Obtener categoría por ID
    @Operation(
        summary = "Obtener categoría por ID",
        description = "Devuelve una categoría específica según su ID"
    )
    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponseDTO> obtenerCategoriaPorId(
            @Parameter(description = "ID de la categoría", required = true)
            @PathVariable Long id) {
        try {
            CategoriaResponseDTO categoria = categoriaService.obtenerCategoriaPorId(id);
            return ResponseEntity.ok(categoria);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    
    // POST - Crear nueva categoría (solo ADMIN y MODERATOR)
    @Operation(
        summary = "Crear nueva categoría",
        description = "Crea una nueva categoría de productos. Requiere permisos de ADMIN o MODERATOR"
    )
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
    public ResponseEntity<?> crearCategoria(@Valid @RequestBody CategoriaCreateDTO categoriaCreateDTO) {
        try {
            CategoriaResponseDTO categoria = categoriaService.crearCategoria(categoriaCreateDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(categoria);
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
    
    // PUT - Actualizar categoría (solo ADMIN y MODERATOR)
    @Operation(
        summary = "Actualizar categoría",
        description = "Actualiza una categoría existente. Requiere permisos de ADMIN o MODERATOR"
    )
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
    public ResponseEntity<?> actualizarCategoria(
            @Parameter(description = "ID de la categoría", required = true)
            @PathVariable Long id,
            @Valid @RequestBody CategoriaCreateDTO categoriaUpdateDTO) {
        try {
            CategoriaResponseDTO categoria = categoriaService.actualizarCategoria(id, categoriaUpdateDTO);
            return ResponseEntity.ok(categoria);
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
    
    // DELETE - Eliminar categoría (solo ADMIN)
    @Operation(
        summary = "Eliminar categoría",
        description = "Elimina una categoría. Solo permitido si no tiene productos asociados. Requiere permisos de ADMIN"
    )
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> eliminarCategoria(
            @Parameter(description = "ID de la categoría", required = true)
            @PathVariable Long id) {
        try {
            categoriaService.eliminarCategoria(id);
            Map<String, String> response = new HashMap<>();
            response.put("mensaje", "Categoría eliminada exitosamente");
            return ResponseEntity.ok(response);
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
    
    // PATCH - Activar categoría (solo ADMIN y MODERATOR)
    @PatchMapping("/{id}/activar")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
    public ResponseEntity<?> activarCategoria(@PathVariable Long id) {
        try {
            categoriaService.activarCategoria(id);
            Map<String, String> response = new HashMap<>();
            response.put("mensaje", "Categoría activada exitosamente");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error interno del servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    // PATCH - Desactivar categoría (solo ADMIN y MODERATOR)
    @PatchMapping("/{id}/desactivar")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
    public ResponseEntity<?> desactivarCategoria(@PathVariable Long id) {
        try {
            categoriaService.desactivarCategoria(id);
            Map<String, String> response = new HashMap<>();
            response.put("mensaje", "Categoría desactivada exitosamente");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error interno del servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    // GET - Buscar categorías por nombre
    @GetMapping("/buscar")
    public ResponseEntity<List<CategoriaResponseDTO>> buscarCategoriasPorNombre(
            @Parameter(description = "Nombre a buscar", required = true)
            @RequestParam String nombre) {
        try {
            List<CategoriaResponseDTO> categorias = categoriaService.buscarCategoriasPorNombre(nombre);
            return ResponseEntity.ok(categorias);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
