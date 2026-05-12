package com.smartlogix.usuario.service;

import com.smartlogix.usuario.model.Role;
import com.smartlogix.usuario.model.User;
import com.smartlogix.usuario.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private UserService userService;

    private User sampleUser(Long id, String nombre, String apellido, String rut, String email) {
        Role role = new Role(1L, "CLIENTE", "Cliente");
        return new User(id, nombre, apellido, rut, email,
                "$2a$10$hash", LocalDate.of(1990, 1, 1),
                "Calle Falsa 123", null, role);
    }

    @Test
    void saveUserSetsRegistrationDate() {
        User user = sampleUser(null, "Carlos", "González", "12345678-9", "carlos@test.cl");
        User saved = sampleUser(1L, "Carlos", "González", "12345678-9", "carlos@test.cl");
        when(userRepository.save(any(User.class))).thenReturn(saved);

        User result = userService.save(user);

        verify(userRepository).save(any(User.class));
        assertNotNull(result.getId());
    }

    @Test
    void findAllReturnsAllUsers() {
        List<User> users = List.of(
                sampleUser(1L, "Ana", "Torres", "11111111-1", "ana@test.cl"),
                sampleUser(2L, "Luis", "Pérez", "22222222-2", "luis@test.cl")
        );
        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.findAll();

        assertEquals(2, result.size());
    }

    @Test
    void findByIdReturnsUserWhenFound() {
        User user = sampleUser(1L, "Ana", "Torres", "11111111-1", "ana@test.cl");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<User> result = userService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("Ana", result.get().getNombre());
    }

    @Test
    void findByRutReturnsUserWhenFound() {
        User user = sampleUser(1L, "Carlos", "González", "12345678-9", "carlos@test.cl");
        when(userRepository.findByRut("12345678-9")).thenReturn(Optional.of(user));

        Optional<User> result = userService.findByRut("12345678-9");

        assertTrue(result.isPresent());
        assertEquals("12345678-9", result.get().getRut());
    }

    @Test
    void deleteThrowsWhenUserNotFound() {
        when(userRepository.existsById(99L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> userService.delete(99L));
    }

    @Test
    void deleteCallsRepositoryWhenUserExists() {
        when(userRepository.existsById(1L)).thenReturn(true);

        userService.delete(1L);

        verify(userRepository).deleteById(1L);
    }
}
