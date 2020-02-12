package com.example;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.verifier.messaging.MessageVerifier;
import org.springframework.cloud.contract.verifier.messaging.boot.AutoConfigureMessageVerifier;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;

//remove::start[]
// remove::end[]
//remove::start[]
// remove::end[]
@SpringBootTest(classes = ProducerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
//remove::start[]
@AutoConfigureMessageVerifier
//remove::end[]
@ImportAutoConfiguration(TestChannelBinderConfiguration.class)
public abstract class BeerMessagingBase {
	//remove::start[]
	@Inject MessageVerifier messaging;
	//remove::end[]
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
