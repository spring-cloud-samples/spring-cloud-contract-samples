package com.example;

//remove::start[]
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

@SpringBootTest(classes = ProducerApplication.class,
		properties = "graphql.servlet.websocket.enabled=false",
		webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//remove::end[]
public abstract class BaseClass {
	//remove::start[]

	@LocalServerPort int port;

	@BeforeEach
	public void setup() {
		RestAssured.baseURI = "http://localhost:" + port;
	}

	//remove::end[]
}
