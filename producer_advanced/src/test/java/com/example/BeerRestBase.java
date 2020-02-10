package com.example;

//remove::start[]
import io.restassured.module.mockmvc.RestAssuredMockMvc;
//remove::end[]

import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.BeforeEach;

public abstract class BeerRestBase {

	@BeforeEach
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
