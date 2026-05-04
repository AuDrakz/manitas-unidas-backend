package manitasUnidas.fichaVet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class FichaVetApplication {
    public static void main(String[] args) {
        SpringApplication.run(FichaVetApplication.class, args);
    }
}