
package com.example;

//remove::start[]
import io.restassured.RestAssured;
// remove::end[]
import org.junit.jupiter.api.BeforeEach;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// tag::annotations[]
@SpringBootTest(classes = BeerRestBase.Config.class,
		webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
		properties = "server.port=0")
public abstract class BeerRestBase {
// end::annotations[]

	@LocalServerPort int port;

	@BeforeEach
	public void setup() {
		//remove::start[]
		RestAssured.baseURI = "http://localhost:" + this.port;
		// remove::end[]
	}

	// tag::config[]
	@Configuration
	@EnableAutoConfiguration
	static class Config {

		@Bean
		PersonCheckingService personCheckingService()  {
			return personToCheck -> personToCheck.age >= 20;
		}

		@Bean
		ProducerController producerController() {
			return new ProducerController(personCheckingService());
		}
	}
	// end::config[]

}
