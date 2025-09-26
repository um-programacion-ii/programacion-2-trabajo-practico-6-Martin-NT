package com.TP6.businessService.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Clase interna para estructurar la respuesta de error
    static class ErrorResponse {
        private int status;              // Código de estado HTTP
        private String message;          // Mensaje de error
        private LocalDateTime timestamp; // Momento en que ocurrió el error

        public ErrorResponse(int status, String message, LocalDateTime timestamp) {
            this.status = status;
            this.message = message;
            this.timestamp = timestamp;
        }

        public int getStatus() { return status; }
        public String getMessage() { return message; }
        public LocalDateTime getTimestamp() { return timestamp; }
    }

    // Manejo de error: categoría no encontrada
    @ExceptionHandler(CategoriaNoEncontradaException.class)
    public ResponseEntity<ErrorResponse> handleCategoriaNoEncontrada(CategoriaNoEncontradaException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    // Manejo de error: validación de negocio fallida
    @ExceptionHandler(ValidacionNegocioException.class)
    public ResponseEntity<ErrorResponse> handleValidacion(ValidacionNegocioException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    // Manejo de error: fallo en comunicación con microservicio externo
    @ExceptionHandler(MicroserviceCommunicationException.class)
    public ResponseEntity<ErrorResponse> handleComunicacion(MicroserviceCommunicationException ex) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    // Manejo de error genérico (cualquier excepción no controlada)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenerico(Exception ex) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Error inesperado: " + ex.getMessage());
    }

    // Metodo común para construir la respuesta de error
    private ResponseEntity<ErrorResponse> buildResponse(HttpStatus status, String message) {
        return new ResponseEntity<>(
                new ErrorResponse(status.value(), message, LocalDateTime.now()),
                status
        );
    }

    // Manejo de error: producto no encontrado
    @ExceptionHandler(ProductoNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> handleProductoNoEncontrado(ProductoNoEncontradoException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    // Manejo de error: inventario no encontrado
    @ExceptionHandler(InventarioNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> handleInventarioNoEncontrado(InventarioNoEncontradoException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

}
