package com.example.intoxication;

//remove::start[]
import io.restassured.module.mockmvc.RestAssuredMockMvc;
//remove::end[]

import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//remove::start[]
import org.springframework.restdocs.JUnitRestDocumentation;
//remove::end[]
import org.springframework.test.context.junit4.SpringRunner;
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
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BeerIntoxicationBase.Config.class)
public abstract class BeerIntoxicationBase {

	private static final String OUTPUT = "target/generated-snippets";

	//remove::start[]
	@Rule
	public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation(OUTPUT);

	@Rule public TestName testName = new TestName();
	//remove::end[]

	@Autowired WebApplicationContext context;

	@Before
	public void setup() {
		//remove::start[]
		RestAssuredMockMvc.mockMvc(MockMvcBuilders.webAppContextSetup(this.context)
				.apply(documentationConfiguration(this.restDocumentation))
				.alwaysDo(document(getClass().getSimpleName() + "_" + testName.getMethodName()))
				.build());
		//remove::end[]
	}

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
			//remove::end[]
			return new Response(previous, current);
		}
	}
	//end::mock[]
}
