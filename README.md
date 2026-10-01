# Taller 2: Servicios Web - API REST de Productos

API REST desarrollada con Spring Boot y Java 21 para la gestión integral de productos en memoria, implementando validaciones de entrada, manejo global de excepciones y pruebas unitarias/integración.

---

## 🛠️ Tecnologías Utilizadas

- **Java**: 21
- **Framework**: Spring Boot 3.3.5
- **Gestor de Dependencias**: Maven
- **Dependencias Principales**:
  - `spring-boot-starter-web` (Desarrollo REST)
  - `spring-boot-starter-validation` (Validaciones Jakarta Bean Validation)
  - `spring-boot-starter-test` (Pruebas con JUnit 5 y MockMvc)

---

## 🚀 Ejecución del Proyecto

1. **Requisitos previos**:
   - JDK 21 instalado y configurado en el `PATH`.
   - Maven 3.8+ (opcional si se utiliza wrapper).

2. **Compilar y ejecutar**:
   ```bash
   mvn clean spring-boot:run
   ```

3. El servicio estará disponible por defecto en:
   ```
   http://localhost:8080/api/productos
   ```

---

## 📋 Endpoints de la API

La API maneja el recurso `/api/productos`:

| Método | Endpoint | Descripción | Código Éxito |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/productos` | Obtiene la lista de todos los productos | `200 OK` |
| `GET` | `/api/productos/{id}` | Busca un producto por su identificador | `200 OK` |
| `POST` | `/api/productos` | Registra un nuevo producto | `201 Created` |
| `PUT` | `/api/productos/{id}` | Actualiza completamente un producto existente | `200 OK` |
| `DELETE` | `/api/productos/{id}` | Elimina un producto por su identificador | `200 OK` |

---

## 📦 Estructura del Modelo `ProductoDto`

```json
{
  "id": 1,
  "nombre": "Laptop Gamer Pro",
  "descripcion": "Laptop con procesador i7 y 16GB RAM para desarrollo y diseño",
  "precio": 1299.99,
  "stock": 10,
  "fechaVencimiento": "2027-12-31",
  "activo": true,
  "categoria": "TECNOLOGIA"
}
```

### Categorías Permitidas:
- `TECNOLOGIA`
- `HOGAR`
- `ALIMENTOS`
- `LIMPIEZA`
- `OTRO`

---

## 🧪 Pruebas y Peticiones HTTP

- En la carpeta [`requests/productos.http`](requests/productos.http) se encuentran ejemplos listos para ser ejecutados mediante extensiones REST Client (VS Code) o IntelliJ HTTP Client.
- Para ejecutar la suite de pruebas automatizadas:
  ```bash
  mvn test
  ```
