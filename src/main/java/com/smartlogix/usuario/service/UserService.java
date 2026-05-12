package com.smartlogix.usuario.service;

import com.smartlogix.usuario.model.User;
import com.smartlogix.usuario.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Servicio de negocio para la entidad User.
 * Patrón de diseño: Service Layer — encapsula la lógica de aplicación.
 */
@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // ===================== CRUD =====================

    public User save(User user) {
        user.setFechaRegistro(LocalDateTime.now());
        return userRepository.save(user);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    /** Búsqueda por RUT (atributo de negocio, no ID de tabla) */
    public Optional<User> findByRut(String rut) {
        return userRepository.findByRut(rut);
    }

    /** Búsqueda por email */
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /** Búsqueda por nombre o apellido (parcial, case-insensitive) */
    public List<User> searchByName(String query) {
        return userRepository
                .findByNombreContainingIgnoreCaseOrApellidoContainingIgnoreCase(query, query);
    }

    public User update(Long id, User user) {
        User existing = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario con ID " + id + " no encontrado."));
        user.setId(id);
        user.setFechaRegistro(existing.getFechaRegistro());
        return userRepository.save(user);
    }

    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Usuario con ID " + id + " no encontrado.");
        }
        userRepository.deleteById(id);
    }
}
