package com.example;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import com.example.beerapiproducerjaxrs.ProducerWithJaxRsApplication;
import org.junit.jupiter.api.BeforeEach;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(classes = {ProducerWithJaxRsApplication.class},
		webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

public abstract class BeerRestBase {

	@LocalServerPort int port;

	public WebTarget webTarget;

	@BeforeEach
	public void setup() {
		this.webTarget = ClientBuilder.newClient().target("http://localhost:" + this.port + "/");
	}
}
