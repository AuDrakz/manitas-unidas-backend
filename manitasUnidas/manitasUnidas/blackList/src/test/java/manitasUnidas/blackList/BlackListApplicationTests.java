package manitasUnidas.blackList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;
import org.springframework.boot.test.context.SpringBootTest;

@Disabled("Saltando el arranque global para evitar caídas por falta de conexión a MySQL")
@SpringBootTest
class BlackListApplicationTests {

	@Test
	void contextLoads() {
	}

}
