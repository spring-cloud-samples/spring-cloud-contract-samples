package com.example;

import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BeerRestBase.Config.class,
		webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
		properties = "server.port=0")
public abstract class BeerRestBase {

	@LocalServerPort int port;

	@Before
	public void setup() {
		RestAssured.baseURI = "http://localhost:" + port;
	}

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

}