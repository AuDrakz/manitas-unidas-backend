package manitasUnidas.refugios;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

@SpringBootTest(properties = {
    "eureka.client.enabled=false",
    "spring.cloud.discovery.enabled=false"
})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class RefugiosApplicationTests {

    @Test
    void contextLoads() {
        // Ahora sí levantará el contexto limpio usando H2 y sin buscar Eureka
    }

}
