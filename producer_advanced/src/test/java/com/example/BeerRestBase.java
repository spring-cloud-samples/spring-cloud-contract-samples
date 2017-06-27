package com.example;

import io.restassured.module.mockmvc.RestAssuredMockMvc;

import org.assertj.core.api.BDDAssertions;
import org.junit.Before;

public abstract class BeerRestBase {

	@Before
	public void setup() {
		//remove::start[]
		RestAssuredMockMvc.standaloneSetup(new StoutController(), new BuyController());
		//remove::end[]
	}

	//remove::start[]
	//tag::assertmethods[]
	protected void assertStatus(String status) {
		BDDAssertions.then(status).isEqualToIgnoringCase(Status.NOT_OK.name());
	}

	protected void assertMessage(String message) {
		BDDAssertions.then(message).contains("Go home!");
	}
	//end::assertmethods[]
	//remove::end[]
}