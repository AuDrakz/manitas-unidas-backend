package manitasUnidas.refugios;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class RefugiosApplication {

    private static final Logger log =
            LoggerFactory.getLogger(RefugiosApplication.class);

    public static void main(String[] args) {

        log.info("Iniciando aplicación Refugios");

        SpringApplication.run(RefugiosApplication.class, args);

        log.info("Aplicación Refugios iniciada correctamente");
    }
}