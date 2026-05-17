# manitas-unidas-backend
[cite_start]Proyecto de microservicios para centro de adopción de mascotas - Desarrollo Fullstack I (DSY1103) [cite: 1, 2]

##  Arquitectura del Sistema e Infraestructura
El sistema está construido bajo una arquitectura distribuida y descentralizada utilizando Spring Cloud para solucionar problemas de escalabilidad en la gestión de adopciones[cite: 2, 10]:
**apiGateway (Puerto 8090):** Centralizador de accesos, manejo de puertos dinámicos y enrutamiento de peticiones a través de predicados de ruta[cite: 44, 59, 62].
**eureka (Puerto 8761):** Servidor de descubrimiento que registra dinámicamente las instancias activas de cada servicio[cite: 45, 57].

## 🚀 Microservicios Implementados
Cada componente opera de manera independiente con su propia persistencia en bases de datos relacionales MySQL, estructurado bajo el patrón de diseño CSR (Controller, Service, Repository):

1. **usuarios:** Gestión y registro de cuentas de usuarios, administradores y posibles adoptantes.
2. **mascotas:** Inventario y catálogo general de los animales rescatados disponibles en el sistema.
3. **refugios:** Administración de los distintos centros de acogida donde residen las mascotas.
4. **fichaVet:** Módulo clínico enfocado en el registro independiente del historial médico, vacunas y controles veterinarios de los animales.
5. **blackList:** Servicio de seguridad perimetral encargado de registrar y sancionar a usuarios no aptos para adopción.
6. **solicitud:** Núcleo transaccional del backend que procesa y gestiona las postulaciones de adopción en el ecosistema.

## 🔗 Integración y Reglas de Negocio (Feign Clients)
La transferencia coherente de datos y las reglas críticas del negocio no funcionan de forma aislada, sino mediante comunicación síncrona usando **OpenFeign**[cite: 46, 48]:
* **ms-solicitud** inyecta clientes declarativos para conectar con los demás servicios en cascada por código.
* Al procesar una adopción, el sistema valida obligatoriamente que el usuario postulante **exista** en `usuarios`, que **no esté bloqueado** en `blackList`, y que la **mascotas** requerida exista en el inventario y esté asociada a un `refugios` válido.

## 📦 Instrucciones para Levantamiento del Proyecto
Para validar el funcionamiento real del sistema, se deben levantar los servicios en las consolas locales siguiendo este orden estricto[cite: 9, 21]:
1.**eureka** (Servidor de descubrimiento activo) [cite: 57]
2. **apiGateway** (Enrutador perimetral operativo) [cite: 59]
3. **Microservicios Core** (`usuarios`, `mascotas`, `refugios`, `blackList`, `fichaVet` y `solicitud`) [cite: 21]
