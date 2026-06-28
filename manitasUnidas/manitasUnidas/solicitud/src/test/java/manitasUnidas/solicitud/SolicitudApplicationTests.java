package manitasUnidas.solicitud;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import static org.junit.jupiter.api.Assertions.assertNotNull;

// Usamos un puerto aleatorio para que no choque con nada
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {
    "eureka.client.enabled=false",
    "spring.cloud.discovery.enabled=false",
    "spring.cloud.config.enabled=false",
    "springdoc.api-docs.enabled=false",
    "spring.jpa.hibernate.ddl-auto=none" // Evita que intente crear tablas
})
class SolicitudApplicationTests {

    @Test
    void contextLoads(ApplicationContext context) {
        // Esta prueba pasa si el contexto de Spring existe, sin importar sus componentes internos
        assertNotNull(context, "El contexto de Spring Boot debería haber cargado correctamente.");
    }
}