package com.example;

import com.jayway.restassured.module.mockmvc.RestAssuredMockMvc;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.WebApplicationContext;

import static com.example.DrunkLevel.DRUNK;
import static com.example.DrunkLevel.SOBER;
import static com.example.DrunkLevel.TIPSY;
import static com.example.DrunkLevel.WASTED;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BeerIntoxicationBase.Config.class)
public abstract class BeerIntoxicationBase {

	@Autowired WebApplicationContext webApplicationContext;

	@Before
	public void setup() {
		RestAssuredMockMvc.webAppContextSetup(webApplicationContext);
	}

	@Configuration
	@EnableAutoConfiguration
	static class Config {

		@Bean
		BeerServingController controller() {
			return new BeerServingController(responseProvider());
		}

		@Bean
		ResponseProvider responseProvider() {
			return new MockResponseProvider();
		}
	}

	static class MockResponseProvider implements ResponseProvider {

		private DrunkLevel previous = SOBER;
		private DrunkLevel current = SOBER;

		@Override public Response thereYouGo(Customer personToCheck) {
			if ("marcin".equals(personToCheck.name)) {
				 switch (current) {
				 case SOBER:
				 	current = TIPSY;
				 	previous = SOBER;
					 break;
				 case TIPSY:
					 current = DRUNK;
					 previous = TIPSY;
					 break;
				 case DRUNK:
					 current = WASTED;
					 previous = DRUNK;
					 break;
				 case WASTED:
					 throw new UnsupportedOperationException("You can't handle it");
				 }
			}
			return new Response(previous, current);
		}
	}
	
}
