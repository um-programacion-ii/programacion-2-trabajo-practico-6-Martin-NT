package com.TP6.microservices_system.data_service.exception;

public class CategoriaYaExisteException extends RuntimeException {
    public CategoriaYaExisteException(String message) {
        super(message);
    }
}
