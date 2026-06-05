package manitasUnidas.seguimiento;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class SeguimientoApplication {

    private static final Logger logger =
            LoggerFactory.getLogger(SeguimientoApplication.class);

    public static void main(String[] args) {

        SpringApplication.run(SeguimientoApplication.class, args);

        logger.info("🚀 Microservicio SEGUIMIENTO iniciado correctamente");
    }
}