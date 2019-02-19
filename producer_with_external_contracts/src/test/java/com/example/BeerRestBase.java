package com.example;

//remove::start[]
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.Before;

//remove::end[]

public abstract class BeerRestBase {
	//remove::start[]
	ProducerController producerController = new ProducerController(oldEnough());

	@Before
	public void setup() {
		RestAssuredMockMvc.standaloneSetup(this.producerController);
	}

	private PersonCheckingService oldEnough() {
		return argument -> argument.age >= 20;
	}
	//remove::end[]
}
