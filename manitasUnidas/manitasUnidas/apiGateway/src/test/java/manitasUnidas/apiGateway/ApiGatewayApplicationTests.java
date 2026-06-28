package manitasUnidas.apiGateway;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Disabled("Test de contexto deshabilitado: el Gateway requiere Eureka activo para levantar correctamente")
class ApiGatewayApplicationTests {

    @Test
    void contextLoads() {
    }
}