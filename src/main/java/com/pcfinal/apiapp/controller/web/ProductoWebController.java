package com.pcfinal.apiapp.controller.web;

import com.pcfinal.apiapp.dto.CategoriaResponseDTO;
import com.pcfinal.apiapp.dto.ProductoCreateDTO;
import com.pcfinal.apiapp.dto.ProductoResponseDTO;
import com.pcfinal.apiapp.dto.ProductoUpdateDTO;
import com.pcfinal.apiapp.service.CategoriaService;
import com.pcfinal.apiapp.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/productos")
public class ProductoWebController {
    
    @Autowired
    private ProductoService productoService;
    
    @Autowired
    private CategoriaService categoriaService;
    
    @GetMapping
    public String listarProductos(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) BigDecimal precioMin,
            @RequestParam(required = false) BigDecimal precioMax,
            @RequestParam(required = false) Long categoriaId,
            @RequestParam(required = false) String filter,
            Model model) {
        
        // Preparar los objetos para los formularios
        model.addAttribute("nuevoProducto", new ProductoCreateDTO());
        model.addAttribute("editProducto", new ProductoUpdateDTO());
        
        // Obtener categorías activas para los selectores
        List<CategoriaResponseDTO> categoriasActivas = categoriaService.obtenerCategoriasActivas();
        model.addAttribute("categoriasActivas", categoriasActivas);
        
        // Obtener todas las categorías para el filtro
        List<CategoriaResponseDTO> todasCategorias = categoriaService.obtenerTodasLasCategorias();
        model.addAttribute("categorias", todasCategorias);
        
        // Filtrar productos según los parámetros
        List<ProductoResponseDTO> productos;
        
        if (nombre != null && !nombre.isEmpty()) {
            productos = productoService.buscarProductosPorNombre(nombre);
            model.addAttribute("searchType", "Productos que contienen: " + nombre);
        } else if (precioMin != null && precioMax != null) {
            productos = productoService.buscarProductosPorRangoPrecio(precioMin, precioMax);
            model.addAttribute("searchType", "Productos con precio entre: $" + precioMin + " y $" + precioMax);
        } else if (categoriaId != null) {
            productos = productoService.buscarProductosPorCategoria(categoriaId);
            CategoriaResponseDTO categoria = categoriaService.obtenerCategoriaPorId(categoriaId);
            model.addAttribute("categoriaSeleccionada", categoria);
        } else if ("con-stock".equals(filter)) {
            productos = productoService.obtenerProductosConStock();
            model.addAttribute("searchType", "Productos con stock disponible");
        } else if ("sin-stock".equals(filter)) {
            productos = productoService.obtenerProductosSinStock();
            model.addAttribute("searchType", "Productos sin stock");
        } else if ("stock-bajo".equals(filter)) {
            productos = productoService.obtenerProductosConStockBajo(5);
            model.addAttribute("searchType", "Productos con stock bajo (5 o menos)");
        } else {
            productos = productoService.obtenerTodosLosProductos();
        }
        
        model.addAttribute("productos", productos);
        return "productos/lista";
    }
    
    @PostMapping("/crear")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public String crearProducto(@Valid @ModelAttribute("nuevoProducto") ProductoCreateDTO productoCreateDTO,
                                BindingResult result, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            return "productos/lista";
        }
        
        try {
            ProductoResponseDTO producto = productoService.crearProducto(productoCreateDTO);
            attributes.addFlashAttribute("mensaje", "Producto creado exitosamente");
        } catch (Exception e) {
            attributes.addFlashAttribute("error", "Error al crear el producto: " + e.getMessage());
        }
        
        return "redirect:/productos";
    }
    
    @PostMapping("/actualizar/{id}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public String actualizarProducto(@PathVariable Long id, 
                                     @Valid @ModelAttribute("editProducto") ProductoUpdateDTO productoUpdateDTO,
                                     BindingResult result, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            return "productos/lista";
        }
        
        try {
            ProductoResponseDTO producto = productoService.actualizarProducto(id, productoUpdateDTO);
            attributes.addFlashAttribute("mensaje", "Producto actualizado exitosamente");
        } catch (Exception e) {
            attributes.addFlashAttribute("error", "Error al actualizar el producto: " + e.getMessage());
        }
        
        return "redirect:/productos";
    }
    
    @GetMapping("/eliminar/{id}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public String eliminarProducto(@PathVariable Long id, RedirectAttributes attributes) {
        try {
            productoService.eliminarProducto(id);
            attributes.addFlashAttribute("mensaje", "Producto eliminado exitosamente");
        } catch (Exception e) {
            attributes.addFlashAttribute("error", "Error al eliminar el producto: " + e.getMessage());
        }
        return "redirect:/productos";
    }
    
    @PostMapping("/stock/{id}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public String actualizarStock(@PathVariable Long id, @RequestParam Integer stock, 
                                 RedirectAttributes attributes) {
        try {
            productoService.actualizarStock(id, stock);
            attributes.addFlashAttribute("mensaje", "Stock actualizado exitosamente");
        } catch (Exception e) {
            attributes.addFlashAttribute("error", "Error al actualizar el stock: " + e.getMessage());
        }
        return "redirect:/productos";
    }
}
