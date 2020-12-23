package com.example;

//remove::start[]
import java.util.Random;

import io.restassured.config.EncoderConfig;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.config.RestAssuredMockMvcConfig;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
//remove::end[]
public abstract class BeerRestBase {
	//remove::start[]
	@Mock PersonCheckingService personCheckingService;
	@Mock StatsService statsService;
	@InjectMocks ProducerController producerController;
	@InjectMocks StatsController statsController;

	@Before
	public void setup() {
		given(this.personCheckingService.shouldGetBeer(argThat(oldEnough()))).willReturn(true);
		given(this.statsService.findBottlesByName(anyString())).willReturn(new Random().nextInt());

		// https://github.com/spring-cloud/spring-cloud-contract/issues/1428
		EncoderConfig encoderConfig = new EncoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false);
		RestAssuredMockMvc.config = new RestAssuredMockMvcConfig().encoderConfig(encoderConfig);
		RestAssuredMockMvc.standaloneSetup(this.producerController, this.statsController);
	}

	private ArgumentMatcher<PersonToCheck> oldEnough() {
		return argument -> argument.age >= 20;
	}
	//remove::end[]
}
