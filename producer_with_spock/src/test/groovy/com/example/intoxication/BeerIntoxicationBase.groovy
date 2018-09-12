package com.example.intoxication

//remove::start[]
import io.restassured.module.mockmvc.RestAssuredMockMvc
//remove::end[]
import spock.lang.Specification

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.context.WebApplicationContext

import static com.example.intoxication.DrunkLevel.DRUNK
import static com.example.intoxication.DrunkLevel.SOBER
import static com.example.intoxication.DrunkLevel.TIPSY
import static com.example.intoxication.DrunkLevel.WASTED

/**
 * Tests for the scenario based stub
 */
@SpringBootTest(classes =  Config)
abstract class BeerIntoxicationBase extends Specification {

	@Autowired
	WebApplicationContext webApplicationContext

	def setup() {
		//remove::start[]
		RestAssuredMockMvc.webAppContextSetup(webApplicationContext)
		//remove::end[]
	}

	@Configuration
	@EnableAutoConfiguration
	static class Config {

		@Bean
		BeerServingController controller() {
			return new BeerServingController(responseProvider())
		}

		@Bean
		ResponseProvider responseProvider() {
			return new MockResponseProvider()
		}
	}

	//tag::mock[]
	static class MockResponseProvider implements ResponseProvider {

		private DrunkLevel previous = SOBER
		private DrunkLevel current = SOBER

		@Override
		Response thereYouGo(Customer personToCheck) {
			//remove::start[]
			if ("marcin".equals(personToCheck.name)) {
				switch (current) {
					case SOBER:
						current = TIPSY
						previous = SOBER
						break
					case TIPSY:
						current = DRUNK
						previous = TIPSY
						break
					case DRUNK:
						current = WASTED
						previous = DRUNK
						break
					case WASTED:
						throw new UnsupportedOperationException("You can't handle it")
				}
			}
			//remove::end[]
			return new Response(previous, current)
		}
	}
	//end::mock[]
}
