package com.example;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
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
@AutoConfigureStubRunner(workOffline = true, ids = "com.example:beer-api-producer")
//remove::end[]
@DirtiesContext
public class BeerStatsControllerTest extends AbstractTest {

	@Autowired MockMvc mockMvc;
	@Autowired BeerStatsController beerStatsController;

	@Value("${stubrunner.runningstubs.beer-api-producer.port}") int producerPort;

	@Before
	public void setupPort() {
		beerStatsController.port = producerPort;
	}

	@Test public void should_return_a_personalized_text_with_amount_of_beers() throws Exception {
		//remove::start[]
		mockMvc.perform(MockMvcRequestBuilders.post("/stats")
				.contentType(MediaType.APPLICATION_JSON)
				.content(statsJson.write(new StatsRequest("marcin")).getJson()))
				.andExpect(status().isOk())
				.andExpect(content().string("Dear marcin thanks for your interested in drinking beer. You've drank <5> beers"));
		//remove::end[]
	}
}

