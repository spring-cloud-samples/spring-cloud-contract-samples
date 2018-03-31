package com.example;

import io.restassured.module.mockmvc.RestAssuredMockMvc;

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
		RestAssuredMockMvc.standaloneSetup(producerController);
	}

	private ArgumentMatcher<PersonToCheck> oldEnough() {
		return argument -> argument.age >= 20;
	}
	//remove::end[]
}
