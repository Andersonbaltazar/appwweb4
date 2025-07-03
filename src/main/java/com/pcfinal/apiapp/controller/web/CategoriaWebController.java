package com.pcfinal.apiapp.controller.web;

import com.pcfinal.apiapp.dto.CategoriaCreateDTO;
import com.pcfinal.apiapp.dto.CategoriaResponseDTO;
import com.pcfinal.apiapp.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/categorias")
public class CategoriaWebController {
    
    @Autowired
    private CategoriaService categoriaService;
    
    @GetMapping
    public String listarCategorias(Model model) {
        List<CategoriaResponseDTO> categorias = categoriaService.obtenerTodasLasCategorias();
        model.addAttribute("categorias", categorias);
        model.addAttribute("nuevaCategoria", new CategoriaCreateDTO());
        return "categorias/lista";
    }
    
    @PostMapping
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public String crearCategoria(@Valid @ModelAttribute("nuevaCategoria") CategoriaCreateDTO categoriaCreateDTO,
                                BindingResult result, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            return "categorias/lista";
        }
        
        try {
            CategoriaResponseDTO categoria = categoriaService.crearCategoria(categoriaCreateDTO);
            attributes.addFlashAttribute("mensaje", "Categoría creada exitosamente");
        } catch (Exception e) {
            attributes.addFlashAttribute("error", "Error al crear la categoría: " + e.getMessage());
        }
        
        return "redirect:/categorias";
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public String obtenerCategoria(@PathVariable Long id, Model model) {
        try {
            CategoriaResponseDTO categoria = categoriaService.obtenerCategoriaPorId(id);
            model.addAttribute("categoria", categoria);
            return "categorias/detalle";
        } catch (Exception e) {
            return "redirect:/categorias";
        }
    }
    
    @PostMapping("/{id}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public String actualizarCategoria(@PathVariable Long id, 
                                     @Valid @ModelAttribute("categoria") CategoriaCreateDTO categoriaCreateDTO,
                                     BindingResult result, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            return "categorias/detalle";
        }
        
        try {
            CategoriaResponseDTO categoria = categoriaService.actualizarCategoria(id, categoriaCreateDTO);
            attributes.addFlashAttribute("mensaje", "Categoría actualizada exitosamente");
        } catch (Exception e) {
            attributes.addFlashAttribute("error", "Error al actualizar la categoría: " + e.getMessage());
        }
        
        return "redirect:/categorias";
    }
    
    @GetMapping("/{id}/activar")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public String activarCategoria(@PathVariable Long id, RedirectAttributes attributes) {
        try {
            categoriaService.activarCategoria(id);
            attributes.addFlashAttribute("mensaje", "Categoría activada exitosamente");
        } catch (Exception e) {
            attributes.addFlashAttribute("error", "Error al activar la categoría: " + e.getMessage());
        }
        return "redirect:/categorias";
    }
    
    @GetMapping("/{id}/desactivar")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public String desactivarCategoria(@PathVariable Long id, RedirectAttributes attributes) {
        try {
            categoriaService.desactivarCategoria(id);
            attributes.addFlashAttribute("mensaje", "Categoría desactivada exitosamente");
        } catch (Exception e) {
            attributes.addFlashAttribute("error", "Error al desactivar la categoría: " + e.getMessage());
        }
        return "redirect:/categorias";
    }
}
