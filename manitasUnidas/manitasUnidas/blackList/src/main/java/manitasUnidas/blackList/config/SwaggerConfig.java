package manitasUnidas.blackList.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Microservicio de Lista Negra (BlackList)") 
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Grupo 3 - Manitas Unidas")
                                .email("naye.leiva@duocuc.cl"))
                        .license(new License()
                                .name("Uso Académico")
                                .url("https://www.duoc.cl")));
    }
}
