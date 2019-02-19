package com.example;

//remove::start[]

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.Before;

//remove::end[]

public abstract class BeerRestBase {
	//remove::start[]
	ProducerController producerController = new ProducerController();

	@Before
	public void setup() {
		RestAssuredMockMvc.standaloneSetup(this.producerController);
	}
	//remove::end[]
}
