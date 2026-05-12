package com.smartlogix.usuario.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para recibir datos de productos desde api-inventario.
 * Patrón de diseño: DTO (Data Transfer Object).
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    private Long id;
    private String name;
    private String sku;
    private int stock;
}
