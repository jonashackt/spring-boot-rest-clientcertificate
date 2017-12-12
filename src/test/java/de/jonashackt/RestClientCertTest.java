package de.jonashackt;

import de.jonashackt.controller.ServerController;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
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
	    
	    Assert.assertEquals(ServerController.RESPONSE, response);
	}
}
