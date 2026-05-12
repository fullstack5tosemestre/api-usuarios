package com.smartlogix.usuario.service;

import com.smartlogix.usuario.model.Role;
import com.smartlogix.usuario.repository.RoleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Servicio de negocio para la entidad Role.
 * Patrón de diseño: Service Layer — separa la lógica de negocio del controlador.
 */
@Service
@Transactional
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    // Create
    public Role save(Role role) {
        return roleRepository.save(role);
    }

    // Read
    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    public Optional<Role> findById(Long id) {
        return roleRepository.findById(id);
    }

    public Optional<Role> findByNombre(String nombre) {
        return roleRepository.findByNombre(nombre);
    }

    // Update
    public Role update(Long id, Role role) {
        if (!roleRepository.existsById(id)) {
            throw new RuntimeException("Rol con ID " + id + " no encontrado.");
        }
        role.setId(id);
        return roleRepository.save(role);
    }

    // Delete
    public void delete(Long id) {
        if (!roleRepository.existsById(id)) {
            throw new RuntimeException("Rol con ID " + id + " no encontrado.");
        }
        roleRepository.deleteById(id);
    }
}
