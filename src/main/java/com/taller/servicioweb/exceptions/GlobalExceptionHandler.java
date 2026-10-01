package com.taller.servicioweb.exceptions;

import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private static final String MENSAJE_DATOS_INVALIDOS = "Los datos enviados no son validos";

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> manejarValidacion(MethodArgumentNotValidException ex) {
        Map<String, String> errores = new LinkedHashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                agregarError(errores, error.getField(), error.getDefaultMessage())
        );

        ErrorResponse respuesta = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                MENSAJE_DATOS_INVALIDOS,
                errores
        );

        return ResponseEntity.badRequest().body(respuesta);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> manejarViolaciones(ConstraintViolationException ex) {
        Map<String, String> errores = new LinkedHashMap<>();

        ex.getConstraintViolations().forEach(violation -> {
            String campo = violation.getPropertyPath().toString();
            int punto = campo.lastIndexOf('.');
            errores.put(punto >= 0 ? campo.substring(punto + 1) : campo, violation.getMessage());
        });

        ErrorResponse respuesta = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                MENSAJE_DATOS_INVALIDOS,
                errores
        );

        return ResponseEntity.badRequest().body(respuesta);
    }

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> manejarRecursoNoEncontrado(RecursoNoEncontradoException ex) {
        ErrorResponse respuesta = new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> manejarJsonInvalido() {
        ErrorResponse respuesta = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "El cuerpo de la solicitud no tiene un formato JSON valido"
        );

        return ResponseEntity.badRequest().body(respuesta);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> manejarTipoDeArgumentoInvalido(
            MethodArgumentTypeMismatchException ex) {
        ErrorResponse respuesta = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "El parametro '" + ex.getName() + "' tiene un valor invalido"
        );

        return ResponseEntity.badRequest().body(respuesta);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> manejarErrorGeneral(Exception ex) {
        LOGGER.error("Error inesperado al procesar la solicitud", ex);

        ErrorResponse respuesta = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Ocurrio un error inesperado"
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);
    }

    private void agregarError(Map<String, String> errores, String campo, String mensaje) {
        String mensajeSeguro = mensaje == null ? "Valor invalido" : mensaje;
        String mensajeActual = errores.get(campo);
        if (mensajeActual == null || mensajeSeguro.toLowerCase().contains("obligatori")) {
            errores.put(campo, mensajeSeguro);
        }
    }
}
