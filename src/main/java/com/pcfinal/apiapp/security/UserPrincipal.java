package com.pcfinal.apiapp.security;

import com.pcfinal.apiapp.entity.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class UserPrincipal implements UserDetails {
    
    private Long id;
    private String username;
    private String email;
    private String password;
    private String nombre;
    private String apellido;
    private Boolean enabled;
    private Collection<? extends GrantedAuthority> authorities;
    
    public UserPrincipal(Long id, String username, String email, String password, String nombre, String apellido, Boolean enabled, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.nombre = nombre;
        this.apellido = apellido;
        this.enabled = enabled;
        this.authorities = authorities;
    }
    
    public static UserPrincipal create(Usuario usuario) {
        List<GrantedAuthority> authorities = usuario.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());
        
        return new UserPrincipal(
                usuario.getId(),
                usuario.getUsername(),
                usuario.getEmail(),
                usuario.getPassword(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getEnabled(),
                authorities
        );
    }
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
    
    @Override
    public String getPassword() {
        return password;
    }
    
    @Override
    public String getUsername() {
        return username;
    }
    
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    
    @Override
    public boolean isEnabled() {
        return enabled != null ? enabled : true;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserPrincipal that = (UserPrincipal) o;
        return Objects.equals(id, that.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    // Getters adicionales
    public Long getId() {
        return id;
    }
    
    public String getEmail() {
        return email;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public String getApellido() {
        return apellido;
    }
}
