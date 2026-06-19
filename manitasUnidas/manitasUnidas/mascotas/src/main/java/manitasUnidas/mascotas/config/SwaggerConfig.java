package manitasUnidas.mascotas.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
<<<<<<< HEAD
=======
import io.swagger.v3.oas.models.info.License;

>>>>>>> d058e8d (Agregar ApiResponse y configuración Swagger)
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
<<<<<<< HEAD
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Microservicio Mascotas")
                        .description("API para gestion de mascotas disponibles para adopcion")
                        .version("1.0")
                        .contact(new Contact()
                                .name("Grupo 3")
                                .email("grupo3@manitasunidas.cl")));
=======
    public OpenAPI mascotasOpenAPI() {

        return new OpenAPI()
                .info(new Info()
                        .title("Microservicio Mascotas")
                        .description("""
                                API REST para la gestión de mascotas
                                del sistema Manitas Unidas.
                                """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Grupo 3 - Manitas Unidas")
                                .email("grupo3@manitasunidas.cl"))
                        .license(new License()
                                .name("Uso Académico")
                                .url("https://www.duoc.cl")));
>>>>>>> d058e8d (Agregar ApiResponse y configuración Swagger)
    }
}