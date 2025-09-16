package com.TP6.microservices_system.data_service.exception;

public class ProductoNoEncontradoException extends RuntimeException {
    public ProductoNoEncontradoException(String message) {
        super(message);
    }
}
