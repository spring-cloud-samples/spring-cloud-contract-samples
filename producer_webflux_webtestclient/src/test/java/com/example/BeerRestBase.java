
package com.example;

//remove::start[]

import io.restassured.module.webtestclient.RestAssuredWebTestClient;
// remove::end[]
import org.junit.jupiter.api.BeforeEach;


public abstract class BeerRestBase {

	@BeforeEach
	public void setup() {
		//remove::start[]
		RestAssuredWebTestClient.standaloneSetup(new ProducerController(personToCheck -> personToCheck.age >= 20));
		// remove::end[]
	}
}
