package com.example;

//remove::start[]
import io.restassured.module.mockmvc.RestAssuredMockMvc;
//remove::end[]
import org.junit.Before;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BeerRestBase.Config.class)
public abstract class BeerRestBase {

	@Autowired
	WebApplicationContext context;

	// remove::start[]
	@Before
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
