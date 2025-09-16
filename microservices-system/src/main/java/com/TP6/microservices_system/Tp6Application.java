package com.example.TP6;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
// Esta anotación habilita el uso de Feign en el proyecto.
// El parámetro "basePackages" debe apuntar al paquete donde estarán tus Feign Clients.
@EnableFeignClients(basePackages = "com.TP6.microservices_system.bussiness_service.client")
public class Tp6Application {
    public static void main(String[] args) {
        SpringApplication.run(Tp6Application.class, args);
    }
}

