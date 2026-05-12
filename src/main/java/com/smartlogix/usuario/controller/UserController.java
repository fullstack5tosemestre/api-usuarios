package com.smartlogix.usuario.controller;

import com.smartlogix.usuario.model.User;
import com.smartlogix.usuario.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controlador REST para la gestión de usuarios.
 * Patrón de diseño: MVC Controller.
 *
 * Endpoints disponibles:
 *   GET    /api/v1/usuarios              → todos los usuarios
 *   GET    /api/v1/usuarios/{id}         → usuario por ID
 *   GET    /api/v1/usuarios/by-rut/{rut} → usuario por RUT
 *   GET    /api/v1/usuarios/by-email     → usuario por email (?email=...)
 *   GET    /api/v1/usuarios/buscar       → búsqueda por nombre/apellido (?q=...)
 *   GET    /api/v1/usuarios/{id}/pedidos → pedidos del usuario (inter-servicio)
 *   POST   /api/v1/usuarios              → crear usuario
 *   PUT    /api/v1/usuarios/{id}         → actualizar usuario
 *   DELETE /api/v1/usuarios/{id}         → eliminar cuenta
 */
@RestController
@RequestMapping("api/v1/usuarios")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> getAll() {
        List<User> users = userService.findAll();
        if (users.isEmpty()) {
            return new ResponseEntity<>(users, HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable Long id) {
        Optional<User> user = userService.findById(id);
        return user.map(u -> new ResponseEntity<>(u, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /** Búsqueda por RUT — atributo de negocio chileno único por persona */
    @GetMapping("/by-rut/{rut}")
    public ResponseEntity<User> getByRut(@PathVariable String rut) {
        Optional<User> user = userService.findByRut(rut);
        return user.map(u -> new ResponseEntity<>(u, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /** Búsqueda por email */
    @GetMapping("/by-email")
    public ResponseEntity<User> getByEmail(@RequestParam String email) {
        Optional<User> user = userService.findByEmail(email);
        return user.map(u -> new ResponseEntity<>(u, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /** Búsqueda de usuarios por nombre o apellido (parcial, case-insensitive) */
    @GetMapping("/buscar")
    public ResponseEntity<List<User>> searchByName(@RequestParam String q) {
        List<User> results = userService.searchByName(q);
        if (results.isEmpty()) {
            return new ResponseEntity<>(results, HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(results, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<User> create(@RequestBody User user) {
        User saved = userService.save(user);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> update(@PathVariable Long id, @RequestBody User user) {
        try {
            User updated = userService.update(id, user);
            return new ResponseEntity<>(updated, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /** Eliminar cuenta de usuario */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            userService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Obtiene los pedidos asociados al usuario.
     * Comunicación inter-servicio: llama a api-pedidos.
     * Se implementa en UserService tras agregar RestTemplate (commit 5).
     */
    @GetMapping("/{id}/pedidos")
    public ResponseEntity<?> getPedidosByUser(@PathVariable Long id) {
        try {
            var pedidos = userService.getOrdersByUser(id);
            return new ResponseEntity<>(pedidos, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
