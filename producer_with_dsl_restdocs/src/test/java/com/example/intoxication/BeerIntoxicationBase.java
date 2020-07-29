package com.example.intoxication;

//remove::start[]

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
//remove::end[]

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//remove::start[]
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
//remove::end[]
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static com.example.intoxication.DrunkLevel.DRUNK;
import static com.example.intoxication.DrunkLevel.SOBER;
import static com.example.intoxication.DrunkLevel.TIPSY;
import static com.example.intoxication.DrunkLevel.WASTED;
//remove::start[]
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
//remove::end[]

/**
 * Tests for the scenario based stub
 */
@SpringBootTest(classes = BeerIntoxicationBase.Config.class)
//remove::start[]
@ExtendWith(RestDocumentationExtension.class)
//remove::end[]
public abstract class BeerIntoxicationBase {

	//remove::start[]
	@Autowired WebApplicationContext context;

	@BeforeEach
	public void setup(RestDocumentationContextProvider provider, TestInfo testInfo) {
		RestAssuredMockMvc.mockMvc(MockMvcBuilders.webAppContextSetup(this.context)
				.apply(documentationConfiguration(provider))
				.alwaysDo(document(getClass().getSimpleName() + "_" + testInfo.getDisplayName()))
				.build());
	}
	//remove::end[]

	@Configuration
	@EnableAutoConfiguration
	static class Config {

		@Bean BeerServingController controller() {
			return new BeerServingController(responseProvider());
		}

		@Bean ResponseProvider responseProvider() {
			return new MockResponseProvider();
		}
	}

	//tag::mock[]
	static class MockResponseProvider implements ResponseProvider {

		private DrunkLevel previous = SOBER;
		private DrunkLevel current = SOBER;

		@Override public Response thereYouGo(Customer personToCheck) {
			//remove::start[]
			if ("marcin".equals(personToCheck.name)) {
				 switch (this.current) {
				 case SOBER:
					 this.current = TIPSY;
					 this.previous = SOBER;
					 break;
				 case TIPSY:
					 this.current = DRUNK;
					 this.previous = TIPSY;
					 break;
				 case DRUNK:
					 this.current = WASTED;
					 this.previous = DRUNK;
					 break;
				 case WASTED:
					 throw new UnsupportedOperationException("You can't handle it");
				 }
			}
			//remove::end[]
			return new Response(this.previous, this.current);
		}
	}
	//end::mock[]
}
