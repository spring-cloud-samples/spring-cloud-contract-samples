package com.example;

//remove::start[]

import java.util.Random;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import name.falgout.jeffrey.testing.junit.mockito.MockitoExtension;
import org.junit.jupiter.api.BeforeEach;
//remove::end[]
import org.junit.jupiter.api.extension.ExtendWith;
//remove::start[]
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.argThat;

//remove::end[]

@ExtendWith(MockitoExtension.class)
public abstract class BeerRestBase {
	//remove::start[]
	@Mock PersonCheckingService personCheckingService;
	@Mock StatsService statsService;
	@InjectMocks ProducerController producerController;
	@InjectMocks StatsController statsController;

	@BeforeEach
	public void setup() {
		given(personCheckingService.shouldGetBeer(argThat(oldEnough()))).willReturn(true);
		given(statsService.findBottlesByName(anyString())).willReturn(new Random().nextInt());
		RestAssuredMockMvc.standaloneSetup(producerController, statsController);
	}

	private ArgumentMatcher<PersonToCheck> oldEnough() {
		return argument -> argument.age >= 20;
	}
	//remove::end[]
}
