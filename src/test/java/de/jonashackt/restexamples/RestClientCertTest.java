package de.jonashackt.restexamples;

import de.jonashackt.restexamples.controller.ServerController;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(
		classes = ServerApplication.class,
		webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
        properties = {"server.port=8443"}
)
public class RestClientCertTest {

	//@LocalServerPort
	private int port = 8443;

	private String baseUrl;

	@Autowired
    private RestTemplate restTemplate;

	@Before
	public void prepare() {
		baseUrl = "https://localhost:" + port;
	}

	@Test
	public void is_hello_resource_callable_with_client_cert() {
		String response = restTemplate.getForObject(baseUrl + "/restexamples/hello", String.class);
	    
	    assertEquals(ServerController.RESPONSE, response);
	}

}
