package com.smartlogix.usuario.controller;

import com.smartlogix.usuario.model.Role;
import com.smartlogix.usuario.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controlador REST para la gestión de roles.
 * Patrón de diseño: MVC Controller — maneja las solicitudes HTTP y delega al servicio.
 * Endpoints: GET (todos / por ID / por nombre), POST, PUT, DELETE.
 */
@RestController
@RequestMapping("api/v1/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping
    public ResponseEntity<List<Role>> getAll() {
        List<Role> roles = roleService.findAll();
        if (roles.isEmpty()) {
            return new ResponseEntity<>(roles, HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(roles, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Role> getById(@PathVariable Long id) {
        Optional<Role> role = roleService.findById(id);
        return role.map(r -> new ResponseEntity<>(r, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/by-nombre/{nombre}")
    public ResponseEntity<Role> getByNombre(@PathVariable String nombre) {
        Optional<Role> role = roleService.findByNombre(nombre);
        return role.map(r -> new ResponseEntity<>(r, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Role> create(@RequestBody Role role) {
        Role saved = roleService.save(role);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Role> update(@PathVariable Long id, @RequestBody Role role) {
        try {
            Role updated = roleService.update(id, role);
            return new ResponseEntity<>(updated, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            roleService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
