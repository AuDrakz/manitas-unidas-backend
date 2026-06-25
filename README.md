# Manitas Unidas — Backend de Microservicios

Sistema de adopción de mascotas desarrollado con arquitectura de microservicios en Spring Boot.

---

## Arquitectura General

```
[Cliente / Postman]
        |
  [API Gateway :8090]
        |
  [Eureka Server :8761]  ← registro y descubrimiento
        |
  ┌─────┴──────────────────────────────────────────────┐
  │  ms-usuarios   ms-mascotas   ms-refugios            │
  │  ms-solicitud  ms-blacklist  ms-fichavet             │
  │  ms-notif.     ms-seguim.    ms-denuncias            │
  │  ms-seguridad                                        │
  └─────────────────────────────────────────────────────┘
```

Cada microservicio tiene su propia base de datos MySQL independiente.

---

## Microservicios — Puertos

| Microservicio     | Nombre en Eureka     | Puerto |
|-------------------|----------------------|--------|
| Eureka Server     | —                    | 8761   |
| API Gateway       | api-gateway          | 8090   |
| Usuarios          | ms-usuarios          | 8081   |
| Mascotas          | ms-mascotas          | 8082   |
| Refugios          | ms-refugios          | 8083   |
| Solicitud         | ms-solicitud         | 8084   |
| BlackList         | ms-blacklist         | 8085   |
| FichaVet          | ms-fichavet          | 8086   |
| Notificaciones    | ms-notificaciones    | 8087   |
| Seguimiento       | ms-seguimiento       | 8088   |
| Denuncias         | ms-denuncias         | 8089   |
| Seguridad         | ms-seguridad         | 8091   |

---

## Requisitos Críticos de Código (Descubrimiento)

Para asegurar que los servicios de negocio se registren correctamente en Eureka, cada clase principal debe incluir:

```java
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class Application {
}
```
---

## Orden de Levantamiento

1. **Inicializar Base de Datos**
   - Abrir XAMPP.
   - Iniciar MySQL en el puerto `3307`.

2. **Compilar el Proyecto**

```cmd
mvn clean package -DskipTests
```

3. **Ejecutar el Script de Arranque**

```cmd
arrancar-nativo.bat
```

4. **Verificar Registro en Eureka**

http://localhost:8761

Todos los microservicios deben aparecer registrados correctamente.

## Bases de Datos
---

Cada servicio usa su propia base de datos en MySQL (puerto 3307):

| Base de Datos        | Microservicio  |
|----------------------|----------------|
| db_usuarios          | ms-usuarios    |
| db_mascotas          | ms-mascotas    |
| db_refugios          | ms-refugios    |
| db_solicitudes       | ms-solicitud   |
| db_blacklist         | ms-blacklist   |
| db_fichavet          | ms-fichavet    |
| db_notificaciones    | ms-notificaciones |
| db_seguimiento       | ms-seguimiento |
| db_denuncias         | ms-denuncias   |
| db_seguridad         | ms-seguridad   |

**Credenciales (desarrollo local):**
- Usuario: `root`
- Contraseña: *(vacía)*
- Puerto MySQL: `3307`

Las bases de datos se crean automáticamente con `createDatabaseIfNotExist=true`.

---

## Endpoints Principales (vía Gateway :8090)

### Usuarios — `/api/usuarios`
| Método | Ruta                       | Descripción                        |
|--------|----------------------------|------------------------------------|
| GET    | /api/usuarios              | Listar todos los usuarios          |
| GET    | /api/usuarios/{id}         | Buscar usuario por ID              |
| POST   | /api/usuarios              | Crear nuevo usuario                |
| PUT    | /api/usuarios/{id}         | Actualizar usuario                 |
| DELETE | /api/usuarios/{id}         | Eliminar usuario                   |
| GET    | /api/usuarios/existe/{id}  | Verificar si existe (uso interno)  |

### Mascotas — `/api/mascotas`
| Método | Ruta                       | Descripción                        |
|--------|----------------------------|------------------------------------|
| GET    | /api/mascotas              | Listar todas las mascotas          |
| GET    | /api/mascotas/{id}         | Buscar mascota por ID              |
| POST   | /api/mascotas              | Registrar mascota (valida usuario y refugio) |
| PUT    | /api/mascotas/{id}         | Actualizar mascota                 |
| DELETE | /api/mascotas/{id}         | Eliminar mascota                   |
| GET    | /api/mascotas/estado/{id}  | Obtener estado de mascota          |
| GET    | /api/mascotas/existe/{id}  | Verificar si existe (uso interno)  |

### Refugios — `/api/refugios`
| Método | Ruta                         | Descripción                    |
|--------|------------------------------|--------------------------------|
| GET    | /api/refugios                | Listar todos                   |
| GET    | /api/refugios/{id}           | Buscar por ID                  |
| POST   | /api/refugios                | Crear refugio                  |
| PUT    | /api/refugios/{id}           | Editar refugio                 |
| DELETE | /api/refugios/{id}           | Eliminar refugio               |
| GET    | /api/refugios/disponibles    | Refugios con cupos disponibles |
| GET    | /api/refugios/cupos/{id}     | Ver cupos de un refugio        |
| GET    | /api/refugios/existe/{id}    | Verificar si existe (interno)  |

### Solicitudes — `/api/solicitudes`
| Método | Ruta                          | Descripción                              |
|--------|-------------------------------|------------------------------------------|
| GET    | /api/solicitudes              | Listar todas las solicitudes             |
| GET    | /api/solicitudes/{id}         | Buscar solicitud por ID                  |
| POST   | /api/solicitudes              | Crear solicitud (valida blacklist, usuario, mascota disponible, no duplicado pendiente) |
| PUT    | /api/solicitudes/{id}/estado  | Cambiar estado (APROBADA/RECHAZADA)      |
| DELETE | /api/solicitudes/{id}         | Eliminar solicitud                       |

### BlackList — `/api/blacklist`
| Método | Ruta                           | Descripción                  |
|--------|--------------------------------|------------------------------|
| GET    | /api/blacklist                 | Listar bloqueados            |
| GET    | /api/blacklist/{id}            | Buscar por ID                |
| GET    | /api/blacklist/verificar/{rut} | Verificar si RUT está bloqueado |
| POST   | /api/blacklist                 | Agregar a la lista           |
| PUT    | /api/blacklist/{id}            | Actualizar registro          |
| DELETE | /api/blacklist/{id}            | Desbloquear                  |

### FichaVet — `/api/fichavet`
| Método | Ruta                               | Descripción                    |
|--------|------------------------------------|--------------------------------|
| GET    | /api/fichavet                      | Listar fichas                  |
| GET    | /api/fichavet/{id}                 | Buscar ficha                   |
| POST   | /api/fichavet                      | Crear ficha                    |
| PUT    | /api/fichavet/{id}                 | Editar ficha                   |
| DELETE | /api/fichavet/{id}                 | Eliminar ficha                 |
| GET    | /api/fichavet/mascota/{idMascota}  | Historial por mascota          |
| GET    | /api/fichavet/veterinario/{rut}    | Fichas por veterinario         |

### Notificaciones — `/notificaciones`
| Método | Ruta                        | Descripción                    |
|--------|-----------------------------|--------------------------------|
| POST   | /notificaciones/enviar      | Enviar notificación            |
| GET    | /notificaciones/historial   | Ver historial de notificaciones |

---

## Roles del Sistema

| Rol         | Permisos                                                   |
|-------------|-------------------------------------------------------------|
| Adoptante   | Ver mascotas, crear solicitudes de adopción                |
| Staff       | Aprobar/rechazar solicitudes, gestionar mascotas           |
| Veterinario | Crear y modificar fichas médicas                           |

---

## Reglas de Negocio Implementadas

- Un adoptante **no puede solicitar adopción** si su RUT está en `ms-blacklist`
- Una mascota **solo puede ser solicitada** si su estado es `Disponible`
- Un adoptante **no puede tener más de una solicitud** en estado `PENDIENTE`
- Al registrar una mascota se valida que el **dueño exista** en `ms-usuarios`
- Al registrar una mascota se valida que el **refugio exista** en `ms-refugios`
- Al crear una solicitud se valida que el **adoptante exista** en `ms-usuarios`

---

## Comunicación entre Microservicios (OpenFeign)

| Desde         | Hacia          | Para qué                              |
|---------------|----------------|---------------------------------------|
| ms-mascotas   | ms-usuarios    | Verificar que el dueño existe         |
| ms-mascotas   | ms-refugios    | Verificar que el refugio existe       |
| ms-solicitud  | ms-blacklist   | Verificar si el RUT está bloqueado    |
| ms-solicitud  | ms-mascotas    | Verificar estado de la mascota        |
| ms-solicitud  | ms-usuarios    | Verificar que el adoptante existe     |

---

## Documentación API (Swagger)

Cada microservicio expone su documentación en:
```
http://localhost:{puerto}/doc/swagger-ui/index.html
```

---

## Tecnologías

### Backend
- Java 21
- Spring Boot 3.5
- Spring Cloud

### Microservicios
- Eureka Server
- API Gateway
- OpenFeign

### Persistencia
- MySQL 8
- Spring Data JPA

### Documentación
- Swagger / OpenAPI

---

# Pruebas Unitarias

El proyecto incorpora pruebas unitarias para validar la lógica de negocio de los microservicios utilizando:

* JUnit 5
* Mockito
* Spring Boot Test

Ejecutar pruebas:

```bash
mvn test
```

Compilar sin ejecutar pruebas:

```bash
mvn clean package -DskipTests
```

---

# Script de Arranque

Para facilitar la ejecución del sistema se incluye el archivo:

```bash
arrancar-nativo.bat
```

Este script permite:

1. Iniciar Eureka Server.
2. Iniciar los microservicios.
3. Iniciar API Gateway.
4. Reducir el tiempo de configuración manual.
5. Levantar la solución completa mediante un único comando.

---

# Verificación en Eureka

Una vez levantado el sistema, acceder a:

```text
http://localhost:8761
```

Deben visualizarse registrados los siguientes servicios:

* API-GATEWAY
* MS-USUARIOS
* MS-MASCOTAS
* MS-REFUGIOS
* MS-SOLICITUD
* MS-BLACKLIST
* MS-FICHAVET
* MS-NOTIFICACIONES
* MS-SEGUIMIENTO
* MS-DENUNCIAS
* MS-SEGURIDAD

Todos los servicios deben encontrarse en estado **UP**.

---

# Evidencia Recomendada

Para facilitar la evaluación del proyecto se recomienda adjuntar capturas de:

* Registro de servicios en Eureka.
* Documentación Swagger.
* Pruebas realizadas con Postman.
* Ejecución de microservicios.
* Bases de datos creadas automáticamente.

---

# Video de Demostración

Enlace del video:

```text
[Agregar enlace del video]
```

El video muestra:

1. Inicio de MySQL.
2. Ejecución de arrancar-nativo.bat.
3. Registro de servicios en Eureka.
4. Uso de Swagger.
5. Pruebas de endpoints mediante Postman.
6. Comunicación entre microservicios.
7. Validación de reglas de negocio.

---

# Subtítulos

El video incorpora subtítulos explicativos para facilitar la comprensión de:

* Arquitectura basada en microservicios.
* Registro y descubrimiento mediante Eureka.
* Enrutamiento mediante API Gateway.
* Comunicación entre servicios con OpenFeign.
* Persistencia de datos en MySQL.
* Validaciones y reglas de negocio implementadas.

---

# Repositorio

Repositorio GitHub del proyecto:

```text
[Agregar enlace del repositorio]
```

---

# Integrantes

* Alan Ojeda 1
* Nayeli 2
* Javier Molina 3

---
