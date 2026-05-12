# api-usuario — SmartLogix

Microservicio REST para la gestión de usuarios de la plataforma SmartLogix E-Commerce.  
Construido con **Spring Boot 3.3.4**, **Spring Data JPA**, **Liquibase** y **MySQL**.

## Patrones de diseño utilizados

- **Repository** — abstracción de acceso a datos con Spring Data JPA.
- **Service Layer** — separa la lógica de negocio del controlador.
- **DTO (Data Transfer Object)** — desacopla las entidades internas de los datos consumidos de otros microservicios.
- **Factory Method** — Spring gestiona el bean `RestTemplate` como factory.

## Arquitectura

```
api-gateway  →  api-usuario  →  api-pedidos
                           └→  api-inventario
```

El microservicio expone sus endpoints bajo `/api/v1/usuarios` y `/api/v1/roles`,  
y se comunica con **api-pedidos** (puerto 8081) y **api-inventario** (puerto 9090)  
mediante `RestTemplate`.

## Base de datos

- Motor: **MySQL 8.0** (bd propia: `db_usuario`)
- Migraciones: **Liquibase** — gestiona schema y datos semilla
- Tablas: `roles` ↔ `users` (relación ManyToOne)

## Endpoints principales

### Usuarios (`/api/v1/usuarios`)

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/api/v1/usuarios` | Todos los usuarios |
| GET | `/api/v1/usuarios/{id}` | Usuario por ID |
| GET | `/api/v1/usuarios/by-rut/{rut}` | Búsqueda por RUT |
| GET | `/api/v1/usuarios/by-email?email=...` | Búsqueda por email |
| GET | `/api/v1/usuarios/buscar?q=...` | Búsqueda por nombre/apellido |
| GET | `/api/v1/usuarios/{id}/pedidos` | Pedidos del usuario (→ api-pedidos) |
| GET | `/api/v1/usuarios/catalogo` | Catálogo de productos (→ api-inventario) |
| POST | `/api/v1/usuarios` | Registrar usuario |
| PUT | `/api/v1/usuarios/{id}` | Actualizar usuario |
| DELETE | `/api/v1/usuarios/{id}` | Eliminar cuenta |

### Roles (`/api/v1/roles`)

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/api/v1/roles` | Todos los roles |
| GET | `/api/v1/roles/{id}` | Rol por ID |
| GET | `/api/v1/roles/by-nombre/{nombre}` | Rol por nombre |
| POST | `/api/v1/roles` | Crear rol |
| PUT | `/api/v1/roles/{id}` | Actualizar rol |
| DELETE | `/api/v1/roles/{id}` | Eliminar rol |

## Documentación Swagger

Una vez levantado el servicio: [http://localhost:8082/swagger-ui.html](http://localhost:8082/swagger-ui.html)

## Configuración local

1. Levantar MySQL (Docker):
   ```bash
   bash run_db.sh
   ```

2. Copiar variables de entorno:
   ```bash
   cp .env.example .env
   # Editar .env con tu URL y password de MySQL
   ```

3. Compilar y ejecutar:
   ```bash
   ./mvnw spring-boot:run
   ```

## Ejecutar con Docker

```bash
./mvnw clean package -DskipTests
docker build -t xdainz/api-usuario .
bash deploy.sh
```

## Pruebas unitarias

```bash
./mvnw test
```

## Variables de entorno (`.env`)

```
SPRING_DATASOURCE_URL=jdbc:mysql://<host>:3306/db_usuario?createDatabaseIfNotExist=true
SPRING_DATASOURCE_PASSWORD=<password>
```
