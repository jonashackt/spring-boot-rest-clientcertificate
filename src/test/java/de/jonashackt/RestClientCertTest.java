package de.jonashackt;

import de.jonashackt.controller.ServerController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest(
		classes = ServerApplication.class,
		webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class RestClientCertTest {

	@LocalServerPort
	private int port;

	@Autowired
    private RestTemplate restTemplate;

	@Test
	public void is_hello_resource_callable_with_client_cert() {
		String response = restTemplate.getForObject("https://localhost:" + port + "/restexamples/hello", String.class);
	    
	    assertEquals(ServerController.RESPONSE, response);
	}
}
