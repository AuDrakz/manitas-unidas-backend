package manitasUnidas.usuarios.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI usuariosOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Microservicio Usuarios")
                        .description("API para gestión de usuarios del sistema Manitas Unidas")
                        .version("1.0")
                        .contact(new Contact()
                                .name("Grupo 3")
                                .email("grupo3@manitasunidas.cl")));
    }
}