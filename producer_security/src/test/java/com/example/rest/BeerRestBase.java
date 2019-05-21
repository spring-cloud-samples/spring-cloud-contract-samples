package com.example.rest;

// remove::start[]

import com.example.ProducerApplication;
import com.example.security.UserDetails;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.Before;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { ProducerApplication.class,
		BeerRestBase.Config.class }, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
// remove::end[]
public abstract class BeerRestBase {

	// remove::start[]
	@Autowired
	private WebApplicationContext context;

	@Autowired
	private FilterChainProxy springSecurityFilterChain;

	@Before
	public void setup() {
		RestAssuredMockMvc
				.standaloneSetup(MockMvcBuilders.webAppContextSetup(this.context)
						.addFilter(this.springSecurityFilterChain));
	}

	@Configuration
	static class Config {

		// We want to override the default behaviour
		@Bean
		@Primary
		PersonCheckingService myPersonCheckingService() {
			return new PersonCheckingService() {
				@Override
				public Boolean shouldGetBeer(UserDetails userDetails) {
					return userDetails.getAge() >= 21;
				}
			};
		}

	}
	// remove::end[]

}
