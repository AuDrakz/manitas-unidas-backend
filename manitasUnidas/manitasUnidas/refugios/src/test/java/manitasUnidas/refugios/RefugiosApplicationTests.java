package manitasUnidas.refugios;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

@SpringBootTest(properties = {
    "eureka.client.enabled=false",
    "spring.cloud.discovery.enabled=false",
    // Agregamos la exclusión aquí para que deshaga el conflicto de HATEOAS con Spring Boot 3.5+
    "spring.autoconfigure.exclude=org.springdoc.core.configuration.SpringDocHateoasConfiguration"
})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class RefugiosApplicationTests {

    @Test
    void contextLoads() {
        // Ahora sí levantará el contexto limpio usando H2, sin buscar Eureka y sin romper el método de HATEOAS
    }

}
