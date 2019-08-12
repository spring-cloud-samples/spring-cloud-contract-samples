package com.example;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
//remove::start[]
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerPort;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
//remove::end[]
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Marcin Grzejszczak
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
//remove::start[]
@AutoConfigureStubRunner(stubsMode = StubRunnerProperties.StubsMode.LOCAL, ids = "com.example:beer-api-producer-java")
//remove::end[]
@DirtiesContext
public class BeerStatsControllerTest extends AbstractTest {

	@Autowired MockMvc mockMvc;
	@Autowired BeerStatsController beerStatsController;

	//remove::start[]
	@StubRunnerPort("beer-api-producer-java") int producerPort;
	//remove::end[]

	@Before
	public void setupPort() {
		//remove::start[]
		this.beerStatsController.port = this.producerPort;
		//remove::start[]
	}

	@Test public void should_return_a_personalized_text_with_amount_of_beers() throws Exception {
		//remove::start[]
		this.mockMvc.perform(MockMvcRequestBuilders.post("/stats")
				.contentType(MediaType.APPLICATION_JSON)
				.content(this.statsJson.write(new StatsRequest("marcin")).getJson()))
				.andExpect(status().isOk())
				.andExpect(content().string("Dear marcin thanks for your interested in drinking beer. You've drank <5> beers"));
		//remove::end[]
	}
}
