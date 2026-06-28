package manitasUnidas.notificaciones;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled; // <-- Línea agregada
import org.springframework.boot.test.context.SpringBootTest;

@Disabled("Saltando el arranque para evitar caídas por falta de conexión a MySQL") // <-- Línea agregada
@SpringBootTest
class NotificacionesApplicationTests {

    @Test
    void contextLoads() {
    }
}