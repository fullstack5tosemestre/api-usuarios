package com.smartlogix.usuario.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.smartlogix.usuario.dto.OrderDTO;
import com.smartlogix.usuario.model.Role;
import com.smartlogix.usuario.model.User;
import com.smartlogix.usuario.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    private User sampleUser(Long id, String nombre, String apellido, String rut) {
        Role role = new Role(2L, "CLIENTE", "Cliente");
        return new User(id, nombre, apellido, rut,
                nombre.toLowerCase() + "@test.cl", "$2a$10$hash",
                LocalDate.of(1990, 5, 20), "Calle Test 1",
                LocalDateTime.now(), role);
    }

    @Test
    void getAllReturnsOkWhenUsersExist() throws Exception {
        when(userService.findAll()).thenReturn(
                List.of(sampleUser(1L, "Ana", "Torres", "11111111-1")));

        mockMvc.perform(get("/api/v1/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Ana"));
    }

    @Test
    void getAllReturnsNoContentWhenEmpty() throws Exception {
        when(userService.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/usuarios"))
                .andExpect(status().isNoContent());
    }

    @Test
    void getByIdReturnsOkWhenFound() throws Exception {
        when(userService.findById(1L))
                .thenReturn(Optional.of(sampleUser(1L, "Ana", "Torres", "11111111-1")));

        mockMvc.perform(get("/api/v1/usuarios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void getByIdReturnsNotFoundWhenMissing() throws Exception {
        when(userService.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/usuarios/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getByRutReturnsOkWhenFound() throws Exception {
        when(userService.findByRut("12345678-9"))
                .thenReturn(Optional.of(sampleUser(1L, "Carlos", "González", "12345678-9")));

        mockMvc.perform(get("/api/v1/usuarios/by-rut/12345678-9"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rut").value("12345678-9"));
    }

    @Test
    void createReturnsCreated() throws Exception {
        User request = sampleUser(null, "Luis", "Pérez", "33333333-3");
        User saved = sampleUser(5L, "Luis", "Pérez", "33333333-3");
        when(userService.save(any(User.class))).thenReturn(saved);

        mockMvc.perform(post("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(5));
    }

    @Test
    void deleteReturnsNoContent() throws Exception {
        mockMvc.perform(delete("/api/v1/usuarios/1"))
                .andExpect(status().isNoContent());

        verify(userService).delete(1L);
    }

    @Test
    void deleteReturnsNotFoundWhenServiceThrows() throws Exception {
        doThrow(new RuntimeException("not found")).when(userService).delete(99L);

        mockMvc.perform(delete("/api/v1/usuarios/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getPedidosByUserReturnsOkWithPedidos() throws Exception {
        List<OrderDTO> pedidos = List.of(
                new OrderDTO(1L, "Ana Torres", "PENDIENTE", LocalDateTime.now()));
        when(userService.getOrdersByUser(1L)).thenReturn(pedidos);

        mockMvc.perform(get("/api/v1/usuarios/1/pedidos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("PENDIENTE"));
    }
}
