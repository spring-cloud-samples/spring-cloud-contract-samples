package com.example;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.Before;

public abstract class BeerRestBase {

	@Before
	public void setup() {
		RestAssuredMockMvc.standaloneSetup(new ProducerController(service()));
	}

	private PersonCheckingService service() {
		return personToCheck -> personToCheck.age >= 20;
	}
}
