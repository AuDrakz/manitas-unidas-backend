package manitasUnidas.blackList;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient

public class BlackListApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlackListApplication.class, args);
	}

}
