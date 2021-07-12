
package com.example;


import io.restassured.module.webtestclient.RestAssuredWebTestClient;
import org.junit.jupiter.api.BeforeEach;


public abstract class BeerRestBase {

	@BeforeEach
	public void setup() {
		
		RestAssuredWebTestClient.standaloneSetup(new ProducerController(personToCheck -> personToCheck.age >= 20));
		
	}
}
