package com.smartlogix.usuario.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Configuración del cliente HTTP para comunicación entre microservicios.
 * Patrón de diseño: Factory Method — Spring actúa como fábrica del bean RestTemplate.
 */
@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
