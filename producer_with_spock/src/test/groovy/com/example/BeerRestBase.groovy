package com.example

import io.restassured.module.mockmvc.RestAssuredMockMvc
import spock.lang.Specification

abstract class BeerRestBase extends Specification {
	//remove::start[]
	PersonCheckingService personCheckingService = Mock(PersonCheckingService)
	StatsService statsService = Mock(StatsService)
	ProducerController producerController = new ProducerController(personCheckingService)
	StatsController statsController = new StatsController(statsService)

	def setup() {
		personCheckingService.shouldGetBeer(_ as PersonToCheck) >> true
		statsService.findBottlesByName(_ as String) >> new Random().nextInt()
		RestAssuredMockMvc.standaloneSetup(producerController, statsController)
	}
	//remove::end[]
}
