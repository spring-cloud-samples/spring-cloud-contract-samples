package com.example;

//remove::start[]
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import com.example.beerapiproducerjaxrs.ProducerWithJaxRsApplication;
import org.junit.jupiter.api.BeforeEach;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

@SpringBootTest(classes = {ProducerWithJaxRsApplication.class},
		webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//remove::end[]
public abstract class BeerRestBase {
	//remove::start[]

	@LocalServerPort int port;

	public WebTarget webTarget;

	@BeforeEach
	public void setup() {
		this.webTarget = ClientBuilder.newClient().target("http://localhost:" + this.port + "/");
	}
	//remove::end[]
}
