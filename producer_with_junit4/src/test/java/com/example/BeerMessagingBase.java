package com.example;

//remove::start[]
import javax.inject.Inject;

import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.verifier.messaging.MessageVerifier;
import org.springframework.cloud.contract.verifier.messaging.boot.AutoConfigureMessageVerifier;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ProducerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@AutoConfigureMessageVerifier
@ImportAutoConfiguration(TestChannelBinderConfiguration.class)
//remove::end[]
public abstract class BeerMessagingBase {
	//remove::start[]
	@Inject MessageVerifier messaging;
	@Autowired PersonCheckingService personCheckingService;
	//remove::end[]

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
