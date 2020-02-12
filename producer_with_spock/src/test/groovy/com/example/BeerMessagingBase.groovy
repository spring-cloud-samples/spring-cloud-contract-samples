package com.example

import javax.inject.Inject

import spock.lang.Specification

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
//remove::start[]
import org.springframework.cloud.contract.verifier.messaging.MessageVerifier
import org.springframework.cloud.contract.verifier.messaging.boot.AutoConfigureMessageVerifier
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration

//remove::end[]
@SpringBootTest(classes = ProducerApplication, webEnvironment = SpringBootTest.WebEnvironment.NONE)
//remove::start[]
@AutoConfigureMessageVerifier
@ImportAutoConfiguration(TestChannelBinderConfiguration)
//remove::end[]
abstract class BeerMessagingBase extends Specification {
	//remove::start[]
	@Inject
	MessageVerifier messaging
	//remove::end[]
	@Autowired
	PersonCheckingService personCheckingService

	void clientIsOldEnough() {
		//remove::start[]
		personCheckingService.shouldGetBeer(new PersonToCheck(25))
		//remove::end[]
	}

	void clientIsTooYoung() {
		//remove::start[]
		personCheckingService.shouldGetBeer(new PersonToCheck(5))
		//remove::end[]
	}
}
