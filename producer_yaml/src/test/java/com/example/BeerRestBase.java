package com.example;

//remove::start[]
import java.util.Random;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.Before;

//remove::end[]

public abstract class BeerRestBase {
	//remove::start[]
	ProducerController producerController = new ProducerController(oldEnough());
	StatsController statsController = new StatsController(statsService());

	@Before
	public void setup() {
		RestAssuredMockMvc.standaloneSetup(this.producerController, this.statsController);
	}

	private PersonCheckingService oldEnough() {
		return argument -> argument.age >= 20;
	}

	private StatsService statsService() {
		return name -> new Random().nextInt();
	}
	//remove::end[]
}
