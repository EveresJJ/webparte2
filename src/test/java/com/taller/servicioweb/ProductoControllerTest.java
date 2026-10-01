package com.taller.servicioweb;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taller.servicioweb.dto.CategoriaProducto;
import com.taller.servicioweb.dto.ProductoDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void listarProductosDevuelveArregloJson() throws Exception {
        mockMvc.perform(get("/api/productos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void buscarProductoExistenteDevuelveProducto() throws Exception {
        mockMvc.perform(get("/api/productos/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Laptop academica"));
    }

    @Test
    void crearProductoValidoDevuelveCreated() throws Exception {
        ProductoDto producto = productoValido();

        mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(producto)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/productos/4"))
                .andExpect(jsonPath("$.id").value(4))
                .andExpect(jsonPath("$.nombre").value(producto.getNombre()));
    }

    @Test
    void actualizarProductoValidoDevuelveOk() throws Exception {
        ProductoDto producto = productoValido();
        producto.setNombre("Monitor actualizado");

        mockMvc.perform(put("/api/productos/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(producto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Monitor actualizado"));
    }

    @Test
    void eliminarProductoExistenteDevuelveOk() throws Exception {
        mockMvc.perform(delete("/api/productos/{id}", 2))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value(200))
                .andExpect(jsonPath("$.mensaje").value("Producto eliminado correctamente"))
                .andExpect(jsonPath("$.producto.id").value(2));
    }

    @Test
    void crearProductoInvalidoDevuelveBadRequestConErrores() throws Exception {
        ProductoDto producto = productoInvalido();

        mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(producto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.estado").value(400))
                .andExpect(jsonPath("$.mensaje").value("Los datos enviados no son validos"))
                .andExpect(jsonPath("$.errores", hasKey("nombre")))
                .andExpect(jsonPath("$.errores", hasKey("precio")));
    }

    @Test
    void actualizarProductoInvalidoDevuelveBadRequestConErrores() throws Exception {
        ProductoDto producto = productoInvalido();

        mockMvc.perform(put("/api/productos/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(producto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.estado").value(400))
                .andExpect(jsonPath("$.errores", hasKey("descripcion")))
                .andExpect(jsonPath("$.errores", hasKey("fechaVencimiento")));
    }

    @Test
    void buscarProductoInexistenteDevuelveNotFound() throws Exception {
        mockMvc.perform(get("/api/productos/{id}", 999))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.estado").value(404))
                .andExpect(jsonPath("$.mensaje").value("Producto no encontrado con id 999"));
    }

    @Test
    void buscarConIdNoNumericoDevuelveBadRequest() throws Exception {
        mockMvc.perform(get("/api/productos/{id}", "abc"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.estado").value(400))
                .andExpect(jsonPath("$.mensaje").value("El parametro 'id' tiene un valor invalido"));
    }

    @Test
    void crearConJsonMalFormadoDevuelveBadRequest() throws Exception {
        mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\": \"Producto incompleto\""))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.estado").value(400))
                .andExpect(jsonPath("$.mensaje")
                        .value("El cuerpo de la solicitud no tiene un formato JSON valido"));
    }

    private ProductoDto productoValido() {
        return new ProductoDto(
                null,
                "Monitor profesional",
                "Monitor LED para laboratorio de computacion",
                BigDecimal.valueOf(220.99),
                15,
                LocalDate.now().plusYears(1),
                true,
                CategoriaProducto.ELECTRONICA
        );
    }

    private ProductoDto productoInvalido() {
        return new ProductoDto(
                null,
                "",
                "Corto",
                BigDecimal.ZERO,
                -3,
                LocalDate.now().minusDays(1),
                null,
                null
        );
    }
}
