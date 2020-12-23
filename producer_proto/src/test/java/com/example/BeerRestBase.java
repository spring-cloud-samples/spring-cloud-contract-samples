package com.example;

//remove::start[]
import io.restassured.module.mockmvc.RestAssuredMockMvc;
//remove::end[]
import org.junit.jupiter.api.BeforeEach;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.context.WebApplicationContext;
@SpringBootTest(classes = BeerRestBase.Config.class)
public abstract class BeerRestBase {

	@Autowired
	WebApplicationContext context;

	//remove::start[]
	@BeforeEach
	public void setup() {
		RestAssuredMockMvc.webAppContextSetup(this.context);
	}
	// remove::end[]

	@Configuration
	@EnableAutoConfiguration
	@Import({ ProtoConfiguration.class, ProducerController.class })
	static class Config {

		@Bean
		PersonCheckingService personCheckingService() {
			return argument -> argument.getAge() >= 20;
		}

	}

}
