package manitasUnidas.refugios;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient // Esta es clave para que el API Gateway y Eureka lo vean
public class RefugiosApplication {

    public static void main(String[] args) {
        SpringApplication.run(RefugiosApplication.class, args);
    }

}