package com.TP6.businessService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoResponse {
    private Long id;
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private String categoriaNombre;  // nombre de la categoría
    private Integer stock;           // cantidad en inventario
    private Boolean stockBajo;       // flag de stock bajo
}
