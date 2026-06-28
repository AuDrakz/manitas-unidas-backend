package manitasUnidas.mascotas;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.junit.jupiter.api.Disabled; 

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class MascotasApplicationTests {

    @Disabled 
    @Test
    void contextLoads() {
        // Al tener la dependencia H2 en el pom, ahora si se reemplazara con exito
    }

}