package com.example

import javax.inject.Inject

import org.junit.jupiter.api.Disabled
import spock.lang.Ignore
import spock.lang.Specification

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cloud.contract.verifier.messaging.MessageVerifier
import org.springframework.cloud.contract.verifier.messaging.boot.AutoConfigureMessageVerifier
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration


@SpringBootTest(classes = ProducerApplication, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@AutoConfigureMessageVerifier
@ImportAutoConfiguration(TestChannelBinderConfiguration)
@Ignore("Spring Spock is broken")
abstract class BeerMessagingBase extends Specification {
	@Inject
	MessageVerifier messaging
	@Autowired
	PersonCheckingService personCheckingService

	void clientIsOldEnough() {
		
		personCheckingService.shouldGetBeer(new PersonToCheck(25))
		
	}

	void clientIsTooYoung() {
		
		personCheckingService.shouldGetBeer(new PersonToCheck(5))
		
	}
}
