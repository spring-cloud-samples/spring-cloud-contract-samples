package com.example;

import java.util.concurrent.TimeUnit;
import javax.inject.Inject;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.verifier.messaging.MessageVerifier;
import org.springframework.cloud.contract.verifier.messaging.boot.AutoConfigureMessageVerifier;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ProducerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
//remove::start[]
@AutoConfigureMessageVerifier
//remove::end[]
public abstract class BeerMessagingBase {
	@Inject MessageVerifier messaging;
	@Autowired PersonCheckingService personCheckingService;

	@Before
	public void setup() {
		// let's clear any remaining messages
		// output == destination or channel name
		this.messaging.receive("output", 100, TimeUnit.MILLISECONDS);
	}

	public void clientIsOldEnough() {
		//remove::start[]
		personCheckingService.shouldGetBeer(new PersonToCheck(25));
		//remove::end[]
	}

	public void clientIsTooYoung() {
		//remove::start[]
		personCheckingService.shouldGetBeer(new PersonToCheck(5));
		//remove::end[]
	}
}
