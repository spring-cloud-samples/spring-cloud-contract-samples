package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerPort;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;

import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Marcin Grzejszczak
 */
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@AutoConfigureStubRunner(stubsMode = StubRunnerProperties.StubsMode.LOCAL, ids = "com.example:beer-api-producer-java")

@DirtiesContext
public class BeerStatsControllerTest extends AbstractTest {

	@Autowired MockMvc mockMvc;
	@Autowired BeerStatsController beerStatsController;

	@StubRunnerPort("beer-api-producer-java") int producerPort;

	@BeforeEach
	public void setupPort() {
		
		this.beerStatsController.port = this.producerPort;
		
	}

	@Test public void should_return_a_personalized_text_with_amount_of_beers() throws Exception {
		
		this.mockMvc.perform(MockMvcRequestBuilders.post("/stats")
				.contentType(MediaType.APPLICATION_JSON)
				.content(this.statsJson.write(new StatsRequest("marcin")).getJson()))
				.andExpect(status().isOk())
				.andExpect(content().string("Dear marcin thanks for your interested in drinking beer. You've drank <5> beers"));
		
	}
}
