package com.example;

//remove::start[]

import io.restassured.config.EncoderConfig;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.config.RestAssuredMockMvcConfig;
import org.junit.jupiter.api.BeforeEach;

// remove::end[]

public abstract class BeerRestBase {
	//remove::start[]
	ProducerController producerController = new ProducerController(oldEnough());

	@BeforeEach
	public void setup() {
		// https://github.com/spring-cloud/spring-cloud-contract/issues/1428
		EncoderConfig encoderConfig = new EncoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false);
		RestAssuredMockMvc.config = new RestAssuredMockMvcConfig().encoderConfig(encoderConfig);
		RestAssuredMockMvc.standaloneSetup(this.producerController);
	}

	private PersonCheckingService oldEnough() {
		return argument -> argument.age >= 20;
	}
	//remove::end[]
}
