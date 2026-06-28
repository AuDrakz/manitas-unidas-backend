package manitasUnidas.fichaVet;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;
import org.springframework.boot.test.context.SpringBootTest;

@Disabled("Saltando el arranque global para evitar caídas por falta de conexión a MySQL local")
@SpringBootTest
class FichaVetApplicationTests {

    @Test
    void contextLoads() {
    }
}