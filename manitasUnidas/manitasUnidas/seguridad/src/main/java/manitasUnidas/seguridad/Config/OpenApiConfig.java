package manitasUnidas.seguridad.Config;

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
                // El título grande que reemplaza a "ApiDocumentation"
                .title("API de Seguridad - Proyecto Manitas Unidas") 
                
                // El número de versión que quieras que aparezca al lado
                .version("1.0.0") 
                
                // Una descripción amigable para el usuario que use la API
                .description("Servicio centralizado para la gestión de accesos, registro de usuarios y validación de credenciales con tokens JWT.")
                
                // Opcional: Información de contacto de los desarrolladores
                .contact(new Contact()
                    .name("Equipo de Desarrollo Manitas Unidas")
                    .email("soporte@manitasunidas.cl")
                )
            );
    }
}