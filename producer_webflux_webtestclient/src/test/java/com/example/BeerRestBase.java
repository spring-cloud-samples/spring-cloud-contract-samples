
package com.example;

//remove::start[]

import io.restassured.module.webtestclient.RestAssuredWebTestClient;
// remove::end[]
import org.junit.Before;


public abstract class BeerRestBase {

	@Before
	public void setup() {
		//remove::start[]
		RestAssuredWebTestClient.standaloneSetup(new ProducerController(personToCheck -> personToCheck.age >= 20));
		// remove::end[]
	}
}
