package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.verifier.messaging.boot.AutoConfigureMessageVerifier;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;

//remove::start[]
@SpringBootTest(classes = ProducerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@AutoConfigureMessageVerifier
//remove::end[]
@ImportAutoConfiguration(TestChannelBinderConfiguration.class)
public abstract class BeerMessagingBase {
	@Autowired PersonCheckingService personCheckingService;

	public void clientIsOldEnough() {
		//remove::start[]
		this.personCheckingService.shouldGetBeer(new PersonToCheck(25));
		//remove::end[]
	}

	public void clientIsTooYoung() {
		//remove::start[]
		this.personCheckingService.shouldGetBeer(new PersonToCheck(5));
		//remove::end[]
	}
}
