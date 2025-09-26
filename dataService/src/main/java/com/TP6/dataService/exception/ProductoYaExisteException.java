package com.TP6.dataService.exception;

public class ProductoYaExisteException extends RuntimeException {
    public ProductoYaExisteException(String message) {
        super(message);
    }
}
