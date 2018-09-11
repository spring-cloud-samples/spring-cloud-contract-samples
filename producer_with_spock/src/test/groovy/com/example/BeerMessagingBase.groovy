package com.example

import java.util.concurrent.TimeUnit
import javax.inject.Inject

import spock.lang.Specification

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cloud.contract.verifier.messaging.MessageVerifier
import org.springframework.cloud.contract.verifier.messaging.boot.AutoConfigureMessageVerifier

//remove::start[]

//remove::end[]

//remove::start[]

//remove::end[]

@SpringBootTest(classes = ProducerApplication, webEnvironment = SpringBootTest.WebEnvironment.NONE)
//remove::start[]
@AutoConfigureMessageVerifier
//remove::end[]
abstract class BeerMessagingBase extends Specification {
	//remove::start[]
	@Inject
	MessageVerifier messaging
	//remove::end[]
	@Autowired
	PersonCheckingService personCheckingService

	def setup() {
		// let's clear any remaining messages
		// output == destination or channel name
		//remove::start[]
		messaging.receive("output", 100, TimeUnit.MILLISECONDS)
		//remove::end[]
	}

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
