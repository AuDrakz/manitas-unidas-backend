package manitasUnidas.notificaciones.config;

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
                .title("API de Notificaciones - Proyecto Manitas Unidas") 
                .version("1.0.0") 
                .description("Servicio encargado de gestionar el envío de alertas, correos electrónicos e historial de notificaciones del sistema.")
                .contact(new Contact()
                    .name("Equipo de Desarrollo Manitas Unidas")
                    .email("soporte@manitasunidas.cl")
                )
            );
    }
}