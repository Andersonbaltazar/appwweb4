package com.pcfinal.apiapp.service;

import com.pcfinal.apiapp.dto.JwtResponseDTO;
import com.pcfinal.apiapp.dto.LoginRequestDTO;
import com.pcfinal.apiapp.dto.MessageResponseDTO;
import com.pcfinal.apiapp.dto.SignupRequestDTO;
import com.pcfinal.apiapp.entity.ERole;
import com.pcfinal.apiapp.entity.Rol;
import com.pcfinal.apiapp.entity.Usuario;
import com.pcfinal.apiapp.repository.RolRepository;
import com.pcfinal.apiapp.repository.UsuarioRepository;
import com.pcfinal.apiapp.security.JwtUtils;
import com.pcfinal.apiapp.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class AuthService {
    
    @Autowired
    AuthenticationManager authenticationManager;
    
    @Autowired
    UsuarioRepository usuarioRepository;
    
    @Autowired
    RolRepository rolRepository;
    
    @Autowired
    PasswordEncoder encoder;
    
    @Autowired
    JwtUtils jwtUtils;
    
    public JwtResponseDTO authenticateUser(LoginRequestDTO loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        
        UserPrincipal userDetails = (UserPrincipal) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());
        
        return new JwtResponseDTO(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                userDetails.getNombre(),
                userDetails.getApellido(),
                roles);
    }
    
    public MessageResponseDTO registerUser(SignupRequestDTO signUpRequest) {
        if (usuarioRepository.existsByUsername(signUpRequest.getUsername())) {
            throw new RuntimeException("Error: El username ya está en uso!");
        }
        
        if (usuarioRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new RuntimeException("Error: El email ya está en uso!");
        }
        
        // Crear nueva cuenta de usuario
        Usuario usuario = new Usuario(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()),
                signUpRequest.getNombre(),
                signUpRequest.getApellido());
        
        Set<String> strRoles = signUpRequest.getRoles();
        Set<Rol> roles = new HashSet<>();
        
        if (strRoles == null) {
            Rol userRole = rolRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Rol no encontrado."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Rol adminRole = rolRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Rol no encontrado."));
                        roles.add(adminRole);
                        break;
                    case "mod":
                        Rol modRole = rolRepository.findByName(ERole.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Error: Rol no encontrado."));
                        roles.add(modRole);
                        break;
                    default:
                        Rol userRole = rolRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Rol no encontrado."));
                        roles.add(userRole);
                }
            });
        }
        
        usuario.setRoles(roles);
        usuarioRepository.save(usuario);
        
        return new MessageResponseDTO("Usuario registrado exitosamente!");
    }
    
    public boolean existsByUsername(String username) {
        return usuarioRepository.existsByUsername(username);
    }
    
    public boolean existsByEmail(String email) {
        return usuarioRepository.existsByEmail(email);
    }
}
