package com.example;

//remove::start[]

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;

//remove::end[]

public abstract class BeerRestBase {
	//remove::start[]
	ProducerController producerController = new ProducerController();

	@BeforeEach
	public void setup() {
		RestAssuredMockMvc.standaloneSetup(this.producerController);
	}
	//remove::end[]
}
