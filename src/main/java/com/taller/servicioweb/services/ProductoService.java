package com.taller.servicioweb.services;

import com.taller.servicioweb.dto.CategoriaProducto;
import com.taller.servicioweb.dto.ProductoDto;
import com.taller.servicioweb.exceptions.RecursoNoEncontradoException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ProductoService {

    private final Map<Long, ProductoDto> productos = new ConcurrentHashMap<>();
    private final AtomicLong secuencia = new AtomicLong();

    public ProductoService() {
        crearInicial("Laptop academica", "Equipo portatil para clases y desarrollo de software",
                BigDecimal.valueOf(780.50), 12, CategoriaProducto.ELECTRONICA);
        crearInicial("Kit de limpieza", "Paquete de limpieza para oficinas y laboratorios",
                BigDecimal.valueOf(18.75), 40, CategoriaProducto.LIMPIEZA);
        crearInicial("Silla ergonomica", "Silla ajustable para uso prolongado en escritorio",
                BigDecimal.valueOf(95.99), 8, CategoriaProducto.OFICINA);
    }

    public List<ProductoDto> listar() {
        return productos.values()
                .stream()
                .sorted(Comparator.comparing(ProductoDto::getId))
                .map(this::copiar)
                .toList();
    }

    public ProductoDto buscarPorId(Long id) {
        return copiar(obtenerPorId(id));
    }

    public ProductoDto crear(ProductoDto producto) {
        Long id = secuencia.incrementAndGet();
        ProductoDto nuevoProducto = copiarConId(id, producto);
        productos.put(id, nuevoProducto);
        return copiar(nuevoProducto);
    }

    public ProductoDto actualizar(Long id, ProductoDto producto) {
        obtenerPorId(id);

        ProductoDto productoActualizado = copiarConId(id, producto);
        productos.put(id, productoActualizado);
        return copiar(productoActualizado);
    }

    public ProductoDto eliminar(Long id) {
        ProductoDto eliminado = productos.remove(id);
        if (eliminado == null) {
            throw new RecursoNoEncontradoException("Producto no encontrado con id " + id);
        }
        return copiar(eliminado);
    }

    private void crearInicial(String nombre, String descripcion, BigDecimal precio, Integer stock,
                              CategoriaProducto categoria) {
        ProductoDto producto = new ProductoDto(
                null,
                nombre,
                descripcion,
                precio,
                stock,
                LocalDate.now().plusYears(2),
                true,
                categoria
        );
        crear(producto);
    }

    private ProductoDto obtenerPorId(Long id) {
        ProductoDto producto = productos.get(id);
        if (producto == null) {
            throw new RecursoNoEncontradoException("Producto no encontrado con id " + id);
        }
        return producto;
    }

    private ProductoDto copiarConId(Long id, ProductoDto producto) {
        return new ProductoDto(
                id,
                producto.getNombre(),
                producto.getDescripcion(),
                producto.getPrecio(),
                producto.getStock(),
                producto.getFechaVencimiento(),
                producto.getActivo(),
                producto.getCategoria()
        );
    }

    private ProductoDto copiar(ProductoDto producto) {
        return copiarConId(producto.getId(), producto);
    }
}
