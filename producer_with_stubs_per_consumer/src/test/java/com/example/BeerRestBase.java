package com.example;

//remove::start[]
import io.restassured.module.mockmvc.RestAssuredMockMvc;
//remove::end[]

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.argThat;

@RunWith(MockitoJUnitRunner.class)
public abstract class BeerRestBase {
	//remove::start[]

	@Mock PersonCheckingService personCheckingService;
	@InjectMocks ProducerController producerController;

	@Before
	public void setup() {
		given(personCheckingService.shouldGetBeer(argThat(oldEnough()))).willReturn(true);
		RestAssuredMockMvc.standaloneSetup(producerController,
				new CarRentalHistoryController(someServiceStub()));
	}

	private ArgumentMatcher<PersonToCheck> oldEnough() {
		return argument -> argument.age >= 20;
	}

	private SomeService someServiceStub() {
		return () -> "OK";
	}
	//remove::end[]
}
