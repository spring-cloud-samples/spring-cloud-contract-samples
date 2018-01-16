package com.example;

import io.restassured.module.mockmvc.RestAssuredMockMvc;

import java.util.Random;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.argThat;

@RunWith(MockitoJUnitRunner.class)
public abstract class BeerRestBase {
	//remove::start[]
	@Mock PersonCheckingService personCheckingService;
	@Mock StatsService statsService;
	@InjectMocks ProducerController producerController;
	@InjectMocks StatsController statsController;

	@Before
	public void setup() {
		given(personCheckingService.shouldGetBeer(argThat(oldEnough()))).willReturn(true);
		given(statsService.findBottlesByName(anyString())).willReturn(new Random().nextInt());
		RestAssuredMockMvc.standaloneSetup(producerController, statsController);
	}

	private TypeSafeMatcher<PersonToCheck> oldEnough() {
		return new TypeSafeMatcher<PersonToCheck>() {
			@Override protected boolean matchesSafely(PersonToCheck personToCheck) {
				return personToCheck.age >= 20;
			}

			@Override public void describeTo(Description description) {

			}
		};
	}
	//remove::end[]
}