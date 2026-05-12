package com.smartlogix.usuario.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO para recibir datos de pedidos desde api-pedidos.
 * Patrón de diseño: DTO (Data Transfer Object) — evita acoplamiento
 * directo con la entidad del microservicio remoto.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    private Long id;
    private String customerName;
    private String status;
    private LocalDateTime createdAt;
}
