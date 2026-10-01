package com.taller.servicioweb.exceptions;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ErrorResponse {

    private final int estado;
    private final String mensaje;
    private final Map<String, String> errores;

    public ErrorResponse(int estado, String mensaje) {
        this(estado, mensaje, null);
    }

    public ErrorResponse(int estado, String mensaje, Map<String, String> errores) {
        this.estado = estado;
        this.mensaje = mensaje;
        this.errores = errores;
    }

    public int getEstado() {
        return estado;
    }

    public String getMensaje() {
        return mensaje;
    }

    public Map<String, String> getErrores() {
        return errores;
    }

}
