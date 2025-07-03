package com.pcfinal.apiapp.config;

import com.pcfinal.apiapp.entity.ERole;
import com.pcfinal.apiapp.entity.Rol;
import com.pcfinal.apiapp.entity.Usuario;
import com.pcfinal.apiapp.entity.Categoria;
import com.pcfinal.apiapp.repository.RolRepository;
import com.pcfinal.apiapp.repository.UsuarioRepository;
import com.pcfinal.apiapp.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private RolRepository rolRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private CategoriaRepository categoriaRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public void run(String... args) throws Exception {
        // Crear roles si no existen
        createRoleIfNotExists(ERole.ROLE_USER);
        createRoleIfNotExists(ERole.ROLE_MODERATOR);
        createRoleIfNotExists(ERole.ROLE_ADMIN);
        
        // Crear usuario administrador por defecto si no existe
        createAdminUserIfNotExists();
        
        // Crear categorías por defecto si no existen
        createDefaultCategoriesIfNotExists();
        
        System.out.println("=== Inicialización de datos completada ===");
        System.out.println("Usuario administrador por defecto:");
        System.out.println("Username: admin");
        System.out.println("Password: admin123");
        System.out.println("Categorías creadas por defecto");
        System.out.println("========================================");
    }
    
    private void createRoleIfNotExists(ERole roleName) {
        if (!rolRepository.existsByName(roleName)) {
            Rol rol = new Rol(roleName);
            rolRepository.save(rol);
            System.out.println("Rol creado: " + roleName);
        }
    }
    
    private void createAdminUserIfNotExists() {
        if (!usuarioRepository.existsByUsername("admin")) {
            Usuario admin = new Usuario();
            admin.setUsername("admin");
            admin.setEmail("admin@example.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setNombre("Administrador");
            admin.setApellido("Sistema");
            admin.setEnabled(true);
            
            Set<Rol> roles = new HashSet<>();
            Rol adminRole = rolRepository.findByName(ERole.ROLE_ADMIN)
                    .orElseThrow(() -> new RuntimeException("Error: Rol ADMIN no encontrado."));
            Rol userRole = rolRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Rol USER no encontrado."));
            
            roles.add(adminRole);
            roles.add(userRole);
            admin.setRoles(roles);
            
            usuarioRepository.save(admin);
            System.out.println("Usuario administrador creado: admin / admin123");
        }
    }
    
    private void createDefaultCategoriesIfNotExists() {
        String[] categoriasDefault = {
            "Electrónicos",
            "Ropa y Accesorios", 
            "Hogar y Jardín",
            "Deportes y Recreación",
            "Libros y Medios",
            "Salud y Belleza",
            "Automóvil",
            "Juguetes y Juegos"
        };
        
        String[] descripcionesDefault = {
            "Dispositivos electrónicos, computadoras, smartphones y gadgets",
            "Prendas de vestir, zapatos, joyería y accesorios de moda",
            "Muebles, decoración, herramientas y artículos para el jardín",
            "Equipos deportivos, ropa deportiva y artículos recreativos",
            "Libros, revistas, música, películas y medios digitales",
            "Productos de cuidado personal, cosméticos y suplementos",
            "Repuestos, accesorios y herramientas para automóviles",
            "Juguetes, juegos de mesa y entretenimiento para niños"
        };
        
        for (int i = 0; i < categoriasDefault.length; i++) {
            if (!categoriaRepository.existsByNombreIgnoreCase(categoriasDefault[i])) {
                Categoria categoria = new Categoria(categoriasDefault[i], descripcionesDefault[i]);
                categoriaRepository.save(categoria);
                System.out.println("Categoría creada: " + categoriasDefault[i]);
            }
        }
    }
}
