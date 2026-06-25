package manitasUnidas.mascotas;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class MascotasApplicationTests {

    @Test
    void contextLoads() {
        // Al tener la dependencia H2 en el pom, ahora sí se reemplazará con éxito
    }

}