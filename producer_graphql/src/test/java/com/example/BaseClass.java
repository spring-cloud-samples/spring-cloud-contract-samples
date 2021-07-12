package com.example;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

@SpringBootTest(classes = ProducerApplication.class,
		properties = "graphql.servlet.websocket.enabled=false",
		webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

public abstract class BaseClass {

	@LocalServerPort int port;

	@BeforeEach
	public void setup() {
		RestAssured.baseURI = "http://localhost:" + port;
	}

}
