package com.taller.servicioweb.controllers;

import com.taller.servicioweb.dto.ProductoDto;
import com.taller.servicioweb.dto.RespuestaEliminacionDto;
import com.taller.servicioweb.services.ProductoService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@Validated
@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    public ResponseEntity<List<ProductoDto>> listar() {
        return ResponseEntity.ok(productoService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoDto> buscarPorId(
            @PathVariable @Positive(message = "El id debe ser mayor que cero") Long id) {
        return ResponseEntity.ok(productoService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<ProductoDto> crear(@Valid @RequestBody ProductoDto producto) {
        ProductoDto productoCreado = productoService.crear(producto);
        URI ubicacion = URI.create("/api/productos/" + productoCreado.getId());
        return ResponseEntity.created(ubicacion).body(productoCreado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductoDto> actualizar(
            @PathVariable @Positive(message = "El id debe ser mayor que cero") Long id,
            @Valid @RequestBody ProductoDto producto) {
        return ResponseEntity.ok(productoService.actualizar(id, producto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RespuestaEliminacionDto> eliminar(
            @PathVariable @Positive(message = "El id debe ser mayor que cero") Long id) {
        ProductoDto productoEliminado = productoService.eliminar(id);
        RespuestaEliminacionDto respuesta = new RespuestaEliminacionDto(
                200,
                "Producto eliminado correctamente",
                productoEliminado
        );
        return ResponseEntity.ok(respuesta);
    }
}
