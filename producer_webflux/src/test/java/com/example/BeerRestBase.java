
package com.example;

// remove::start[]
import io.restassured.RestAssured;
// remove::end[]
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringRunner;

// tag::annotations[]
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BeerRestBase.Config.class,
		webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
		properties = "server.port=0")
public abstract class BeerRestBase {
// end::annotations[]

	@LocalServerPort int port;

	@Before
	public void setup() {
		// remove::start[]
		RestAssured.baseURI = "http://localhost:" + port;
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
