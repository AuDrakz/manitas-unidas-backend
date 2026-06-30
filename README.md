# 🐾 MANITAS UNIDAS - SISTEMA DE MICROSERVICIOS

Sistema de gestión de adopción de mascotas desarrollado con arquitectura de microservicios usando Spring Boot y Spring Cloud.

---

##  COMPONENTES DE DISTRIBUCIÓN Y DEFENSA TÉCNICA

Utilice los siguientes enlaces externos para descargar las versiones listas para producción y visualizar la defensa del proyecto:

| Componente | Descripción | Enlace |
| :--- | :--- | :--- |
| **📦 Versión Sin Docker** *(Arranque Nativo)* | Archivo `.zip` con los `.jar` compilados y el script `arrancar-nativo.bat` ordenado por fases. | [Descargar ZIP Nativo aquí](https://drive.google.com/file/d/1OfJcR_y7Ga49PkB233yRWgfjZswnHOVi/view?usp=sharing) |
| **🐳 Versión Con Docker** *(Avance Examen Transversal)* | Archivo `docker-compose.yml` base disponible en el repositorio. Despliegue completo se entregará en el Examen Transversal. | *(Configuración base en el repositorio — ver `docker-compose.yml`)* |
| **🎥 Video de Defensa Técnica** *(Evaluación Individual — 15:08 min)* | Video explicativo donde se evidencia el funcionamiento, testing y aporte técnico individual. | [Ver Video Explicativo aquí](https://drive.google.com/file/d/1Z7W74C-h9jSd4KZ0D1sq5TG5Knql6y_Z/view?usp=sharing) |

---

##  Video explicativo de la defensa

Enlace al video: [Ver video](https://drive.google.com/file/d/1Z7W74C-h9jSd4KZ0D1sq5TG5Knql6y_Z/view?usp=sharing)

---

## Subtítulos o transcripción del video

Archivo de subtítulos: [Ver transcripción](https://drive.google.com/file/d/1a3_2vDo-fl-A0IT2SwWYvKUmvskVPwUt/view?usp=sharing)

---

## 1. Objetivo del proyecto

Manitas Unidas es un sistema de gestión para un centro de adopción de mascotas. Permite administrar usuarios, mascotas, refugios, solicitudes de adopción, fichas veterinarias, seguimiento post-adopción, notificaciones, blacklist de adoptantes y denuncias mediante una arquitectura de microservicios.

El flujo principal es:

1. Registrar usuarios (adoptantes, staff, veterinarios).
2. Registrar mascotas y asociarlas a refugios.
3. Gestionar solicitudes de adopción validando blacklist y disponibilidad.
4. Aprobar/rechazar solicitudes con notificación automática al adoptante.
5. Registrar y actualizar fichas veterinarias (vacunas, tratamientos).
6. Crear seguimiento post-adopción tras cada adopción aprobada.
7. Gestionar denuncias relacionadas al sistema.

---

## Equipo de desarrollo

| Integrante | Rol en el proyecto |
| --- | --- |
| Alan Ojeda | Controladores REST, servicios de negocio, testing unitario y colecciones Postman |
| Nayeli Leiva | Database-per-Service, OpenFeign y documentación Swagger/OpenAPI |
| Javier Molina | Arquitectura multimódulo, empaquetado de JARs y script de arranque nativo |

## 2. Arquitectura general

```text
Cliente externo / Postman / Navegador
        |
        v
API Gateway :8090
        |
        +--> ms-usuarios      :8081  -> db_usuarios
        +--> ms-mascotas      :8082  -> db_mascotas
        +--> ms-refugios      :8083  -> db_refugios
        +--> ms-notificaciones:8084  -> db_notificaciones
        +--> ms-solicitud     :8085  -> db_solicitudes
        +--> ms-fichavet      :8086  -> db_fichavet
        +--> ms-seguimiento   :8087  -> db_seguimiento
        +--> ms-denuncias     :8088  -> db_denuncias
        +--> ms-blacklist     :8089  -> db_blacklist
        +--> seguridad        :8091  -> db_usuarios

Eureka Server :8761
```

---

## 3. Microservicios del sistema

| Módulo             | Puerto | Responsabilidad                              |
| ------------------ | -----: | -------------------------------------------- |
| `eureka`           |   8761 | Registro y descubrimiento de servicios       |
| `apiGateway`       |   8090 | Punto único de entrada a las APIs            |
| `usuarios`         |   8081 | Gestión de usuarios del sistema              |
| `mascotas`         |   8082 | Gestión de mascotas en adopción              |
| `refugios`         |   8083 | Gestión de refugios y centros de adopción    |
| `notificaciones`   |   8084 | Envío de notificaciones a usuarios           |
| `solicitud`        |   8085 | Gestión de solicitudes de adopción           |
| `fichaVet`         |   8086 | Fichas veterinarias, vacunas y tratamientos  |
| `seguimiento`      |   8087 | Seguimiento post-adopción                    |
| `denuncias`        |   8088 | Gestión de denuncias                         |
| `blackList`        |   8089 | Lista negra de adoptantes inhabilitados      |
| `seguridad`        |   8091 | Autenticación y seguridad (JWT)              |

---

## 4. Lógica de negocio y roles

### Roles del sistema

| Rol | Permisos |
| --- | --- |
| **Adoptante** | Puede ver mascotas y solicitar adopciones. |
| **Staff** | Puede aprobar o rechazar solicitudes de adopción y gestionar mascotas. |
| **Veterinario** | Puede actualizar fichas médicas, vacunas y tratamientos. |

### Reglas de negocio

- Un adoptante **no puede adoptar** si su RUT aparece en `ms-blacklist`.
- Una mascota **no puede ser adoptada** si su estado no es `"Disponible"`.
- Una mascota **no puede ser entregada** si tiene vacunas pendientes en `ms-fichavet`.
- Solo el **Staff** puede aprobar o rechazar solicitudes de adopción.
- Solo el **Veterinario** puede modificar fichas médicas, vacunas y tratamientos.
- Cuando una adopción es **aprobada**, `ms-solicitud` cambia el estado a `"Aprobada"` y solicita a `ms-notificaciones` que avise al adoptante.
- Cuando una mascota es adoptada, `ms-mascotas` cambia su estado de `"Disponible"` a `"Adoptado"`.
- Tras una adopción aprobada, `ms-seguimiento` crea un registro de seguimiento post-adopción.
- Un usuario **no puede tener más de una solicitud** de adopción en estado pendiente.

---

## 5. Tecnologías utilizadas

- Java 21
- Spring Boot
- Spring Cloud (Eureka, Gateway, OpenFeign)
- Spring Security / JWT
- Spring Data JPA
- MySQL (puerto 3307 con XAMPP)
- Lombok
- Bean Validation
- Swagger / OpenAPI
- Maven Multi-Módulo
- JUnit 5 / Mockito

---

## 6. Estructura del proyecto

```text
manitas-unidas-backend/
└── manitasUnidas/
    └── manitasUnidas/
        ├── pom.xml                  ← Proyecto padre Maven
        ├── eureka/
        ├── apiGateway/
        ├── usuarios/
        ├── mascotas/
        ├── refugios/
        ├── solicitud/
        ├── seguridad/
        ├── fichaVet/
        ├── notificaciones/
        ├── seguimiento/
        ├── blackList/
        └── denuncias/
```

---

## 7. Bases de datos

Cada microservicio utiliza su propia base de datos (puerto MySQL: `3307`).

| Microservicio  | Base de datos      |
| -------------- | ------------------ |
| `usuarios`     | `db_usuarios`      |
| `mascotas`     | `db_mascotas`      |
| `refugios`     | `db_refugios`      |
| `notificaciones` | `db_notificaciones` |
| `solicitud`    | `db_solicitudes`   |
| `fichaVet`     | `db_fichavet`      |
| `seguimiento`  | `db_seguimiento`   |
| `denuncias`    | `db_denuncias`     |
| `blackList`    | `db_blacklist`     |
| `seguridad`    | `db_usuarios`      |

> Las bases de datos se crean automáticamente si no existen gracias a `createDatabaseIfNotExist=true`.

Si el equipo usa MySQL en el puerto `3306`, cambiar en cada `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/NOMBRE_BD?createDatabaseIfNotExist=true&serverTimezone=UTC
```

---

## 8. Requisitos previos

- Java 17 instalado y en el `PATH`
- XAMPP con MySQL activo en el puerto `3307` (o MySQL en `3306` con ajuste de config)
- Maven instalado (o usar el `mvnw` incluido en cada módulo)

---

## 9. Compilación del proyecto completo

Desde la raíz del proyecto padre (`manitasUnidas/manitasUnidas/`):

```bash
mvn clean install -DskipTests
```

Se usa `-DskipTests` para saltarse los tests durante la compilación inicial. Los tests pueden ejecutarse por separado.

---

## 10. Puesta en marcha sin Docker (Nativa)

### Opción A — Script automático (recomendado)

Desde la raíz del proyecto, ejecutar el script:

```
arrancar-nativo.bat
```

El script levanta los servicios en el orden correcto con tiempos de espera:

1. **Eureka Server** `[8761]` — espera 20 segundos para que esté activo.
2. **Microservicios de negocio** (todos en paralelo): `usuarios`, `seguridad`, `mascotas`, `refugios`, `solicitud`, `fichaVet`, `notificaciones`, `seguimiento`, `blackList`, `denuncias` — espera 35 segundos para el registro en Eureka.
3. **API Gateway** `[8090]`

### Opción B — Manual desde terminal

```bash
# 1. Eureka
cd eureka && mvn spring-boot:run

# 2. Microservicios (en terminales separadas, después de que Eureka esté arriba)
cd usuarios && mvn spring-boot:run
cd mascotas && mvn spring-boot:run
# ... (repetir para cada microservicio)

# 3. API Gateway (último)
cd apiGateway && mvn spring-boot:run
```

### Orden de ejecución

| Orden | Servicio | Puerto |
| ----: | -------- | -----: |
| 1 | `eureka` | 8761 |
| 2 | `usuarios` | 8081 |
| 2 | `seguridad` | 8091 |
| 2 | `mascotas` | 8082 |
| 2 | `refugios` | 8083 |
| 2 | `solicitud` | 8085 |
| 2 | `fichaVet` | 8086 |
| 2 | `notificaciones` | 8084 |
| 2 | `seguimiento` | 8087 |
| 2 | `blackList` | 8089 |
| 2 | `denuncias` | 8088 |
| 3 | `apiGateway` | 8090 |

---

## 11. Eureka Server

La consola de Eureka se encuentra en:

```
http://localhost:8761
```

Con todos los servicios levantados, deben aparecer registrados:

```
API-GATEWAY
MS-USUARIOS
MS-MASCOTAS
MS-REFUGIOS
MS-SOLICITUD
MS-FICHAVET
MS-NOTIFICACIONES
MS-SEGUIMIENTO
MS-BLACKLIST
MS-DENUNCIAS
SEGURIDAD
```

---

## 12. API Gateway

El API Gateway centraliza el acceso en el puerto `8090`:

| Recurso | URL |
| ------- | --- |
| Usuarios | `http://localhost:8090/api/usuarios/**` |
| Mascotas | `http://localhost:8090/api/mascotas/**` |
| Refugios | `http://localhost:8090/api/refugios/**` |
| Solicitudes | `http://localhost:8090/api/solicitudes/**` |
| Ficha Vet | `http://localhost:8090/api/fichavet/**` |
| Notificaciones | `http://localhost:8090/api/notificaciones/**` |
| Seguimiento | `http://localhost:8090/api/seguimientos/**` |
| Blacklist | `http://localhost:8090/api/blacklist/**` |
| Denuncias | `http://localhost:8090/api/denuncias/**` |
| Seguridad/Auth | `http://localhost:8090/auth/**` |

---

## 13. Swagger por microservicio

| Microservicio | Swagger UI |
| ------------- | ---------- |
| `ms-usuarios` | `http://localhost:8081/swagger-ui.html` |
| `ms-mascotas` | `http://localhost:8082/swagger-ui.html` |
| `ms-refugios` | `http://localhost:8083/swagger-ui.html` |
| `ms-notificaciones` | `http://localhost:8084/swagger-ui.html` |
| `ms-solicitud` | `http://localhost:8085/swagger-ui.html` |
| `ms-fichavet` | `http://localhost:8086/swagger-ui.html` |
| `ms-seguimiento` | `http://localhost:8087/swagger-ui.html` |
| `ms-denuncias` | `http://localhost:8088/swagger-ui.html` |
| `ms-blacklist` | `http://localhost:8089/swagger-ui.html` |

---

## 14. Comunicación entre microservicios (OpenFeign)

| Servicio origen | Servicio destino | Objetivo |
| --------------- | ---------------- | -------- |
| `ms-solicitud` | `ms-blacklist` | Verificar que el adoptante no esté inhabilitado |
| `ms-solicitud` | `ms-mascotas` | Verificar disponibilidad de la mascota |
| `ms-solicitud` | `ms-notificaciones` | Notificar al adoptante cuando se resuelve su solicitud |
| `ms-solicitud` | `ms-seguimiento` | Crear seguimiento post-adopción al aprobar |
| `ms-fichavet` | `ms-mascotas` | Asociar ficha veterinaria a la mascota |
| `ms-denuncias` | `ms-usuarios` | Validar que el usuario denunciante exista |

---

## 15. Pruebas unitarias

Las pruebas unitarias se implementaron con JUnit 5 y Mockito en cada microservicio.

Para ejecutar los tests de un microservicio específico:

```bash
cd usuarios
mvn test
```

Para ejecutar todos los tests desde la raíz del proyecto padre:

```bash
mvn test
```

---

## 16. Comandos útiles

```bash
# Compilar todo el proyecto (sin tests)
mvn clean install -DskipTests

# Ejecutar un microservicio desde terminal
cd usuarios
mvn spring-boot:run

# Compilar solo un módulo
mvn clean install -pl usuarios -DskipTests

# Compilar un módulo y sus dependencias
mvn clean install -pl usuarios -am -DskipTests

# Ejecutar tests de un módulo
mvn test -pl usuarios
```

---

## 17. Estado actual del proyecto

| Elemento | Estado |
| --- | --- |
| Proyecto padre Maven | ✅ Implementado |
| Bases de datos MySQL | ✅ Implementadas |
| Eureka Server | ✅ Implementado |
| API Gateway | ✅ Implementado |
| `ms-usuarios` | ✅ Implementado |
| `ms-mascotas` | ✅ Implementado |
| `ms-refugios` | ✅ Implementado |
| `ms-solicitud` | ✅ Implementado |
| `ms-fichavet` | ✅ Implementado |
| `ms-notificaciones` | ✅ Implementado |
| `ms-seguimiento` | ✅ Implementado |
| `ms-blacklist` | ✅ Implementado |
| `ms-denuncias` | ✅ Implementado |
| `seguridad` (JWT) | ✅ Implementado |
| Swagger por microservicio | ✅ Implementado |
| Feign Client | ✅ Implementado |
| Manejo de errores | ✅ Implementado |
| Pruebas unitarias | ✅ Implementadas |
| Docker Compose | ⏳ Para Examen Transversal |