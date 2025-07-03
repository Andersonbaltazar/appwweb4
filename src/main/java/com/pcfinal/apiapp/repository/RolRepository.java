package com.pcfinal.apiapp.repository;

import com.pcfinal.apiapp.entity.ERole;
import com.pcfinal.apiapp.entity.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface RolRepository extends JpaRepository<Rol, Long> {
    
    Optional<Rol> findByName(ERole name);
    
    Boolean existsByName(ERole name);
}
