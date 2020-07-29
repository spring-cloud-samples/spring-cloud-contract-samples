package com.example

//remove::start[]
import io.restassured.config.EncoderConfig
import io.restassured.module.mockmvc.RestAssuredMockMvc
import io.restassured.module.mockmvc.config.RestAssuredMockMvcConfig
//remove::end[]
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
		// https://github.com/spring-cloud/spring-cloud-contract/issues/1428
		EncoderConfig encoderConfig = new EncoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false);
		RestAssuredMockMvc.config = new RestAssuredMockMvcConfig().encoderConfig(encoderConfig);
		RestAssuredMockMvc.standaloneSetup(producerController, statsController)
	}
	//remove::end[]
}
