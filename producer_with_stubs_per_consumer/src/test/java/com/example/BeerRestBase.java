package com.example;

//remove::start[]
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;

//remove::end[]

public abstract class BeerRestBase {
	//remove::start[]

	ProducerController producerController = new ProducerController(oldEnough());

	@BeforeEach
	public void setup() {
		RestAssuredMockMvc.standaloneSetup(this.producerController,
				new CarRentalHistoryController(someServiceStub()));
	}

	private PersonCheckingService oldEnough() {
		return argument -> argument.age >= 20;
	}

	private SomeService someServiceStub() {
		return () -> "OK";
	}
	//remove::end[]
}
