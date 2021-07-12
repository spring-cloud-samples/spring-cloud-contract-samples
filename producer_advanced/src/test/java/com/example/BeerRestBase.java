package com.example;


import io.restassured.config.EncoderConfig;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.config.RestAssuredMockMvcConfig;

import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.BeforeEach;


public abstract class BeerRestBase {

	@BeforeEach
	public void setup() {
		
		// https://github.com/spring-cloud/spring-cloud-contract/issues/1428
		EncoderConfig encoderConfig = new EncoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false);
		RestAssuredMockMvc.config = new RestAssuredMockMvcConfig().encoderConfig(encoderConfig);
		RestAssuredMockMvc.standaloneSetup(new StoutController(), new BuyController());
		
	}

	
	protected void assertStatus(String status) {
		BDDAssertions.then(status).isEqualToIgnoringCase(Status.NOT_OK.name());
	}

	protected void assertMessage(String message) {
		BDDAssertions.then(message).contains("Go home!");
	}
	
}
