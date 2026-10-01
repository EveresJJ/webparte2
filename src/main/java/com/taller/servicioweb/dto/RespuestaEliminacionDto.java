package com.taller.servicioweb.dto;

public record RespuestaEliminacionDto(
        int estado,
        String mensaje,
        ProductoDto producto
) {
}
