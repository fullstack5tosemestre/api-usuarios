package com.smartlogix.usuario.repository;

import com.smartlogix.usuario.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repositorio JPA para la entidad Role.
 * Patrón de diseño: Repository.
 */
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByNombre(String nombre);
}
