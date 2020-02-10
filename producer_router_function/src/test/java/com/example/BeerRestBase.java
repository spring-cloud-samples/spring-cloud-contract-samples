
package com.example;

import io.restassured.module.webtestclient.RestAssuredWebTestClient;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.test.web.reactive.server.WebTestClient;

public abstract class BeerRestBase {

	@BeforeEach
	public void setup() {
		//remove::start[]
		RestAssuredWebTestClient.webTestClient(WebTestClient.bindToRouterFunction(
			new ProducerConfiguration(personToCheck -> personToCheck.age >= 20).route()).build());
		// remove::end[]
	}
}
