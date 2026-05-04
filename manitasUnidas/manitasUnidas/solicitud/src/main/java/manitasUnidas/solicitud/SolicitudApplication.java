package manitasUnidas.solicitud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients; 

@SpringBootApplication
@EnableFeignClients // <--- ESTO ACTIVA LA COMUNICACIÓN CON OTROS SERVICIOS
public class SolicitudApplication {
    public static void main(String[] args) {
        SpringApplication.run(SolicitudApplication.class, args);
    }
}