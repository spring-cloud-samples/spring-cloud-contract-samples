package com.example;

import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.verifier.messaging.boot.AutoConfigureMessageVerifier;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ProducerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@AutoConfigureMessageVerifier
@ImportAutoConfiguration(TestChannelBinderConfiguration.class)
public abstract class BeerMessagingBase {

	@Autowired PersonCheckingService personCheckingService;

	public void clientIsOldEnough() {
		
		this.personCheckingService.shouldGetBeer(new PersonToCheck(25));
		
	}

	public void clientIsTooYoung() {
		
		this.personCheckingService.shouldGetBeer(new PersonToCheck(5));
		
	}
}
