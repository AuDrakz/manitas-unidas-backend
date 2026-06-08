package manitasUnidas.usuarios.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                // Título descriptivo e institucional que sustituye a ApiDocumentation
                .title("API de Usuarios - Proyecto Manitas Unidas") 
                .version("1.0.0") 
                .description("Servicio centralizado para administrar las cuentas de usuario, sus datos de contacto (RUT, nombre, correo) y roles asignados dentro de la plataforma.")
                .contact(new Contact()
                    .name("Equipo de Desarrollo Manitas Unidas")
                    .email("soporte@manitasunidas.cl")
                )
            );
    }
}