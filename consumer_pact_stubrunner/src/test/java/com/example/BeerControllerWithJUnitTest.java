package com.example;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.cloud.contract.stubrunner.junit.StubRunnerRule;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
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
@DirtiesContext
//@org.junit.Ignore
public class BeerControllerWithJUnitTest extends AbstractTest {

	@Autowired MockMvc mockMvc;
	@Autowired BeerController beerController;
	//remove::start[]
	// tag::rule[]
	@Rule public StubRunnerRule rule = new StubRunnerRule()
			.repoRoot("pact://http://localhost:8085")
			.downloadStub("com.example","beer-api-producer-pact")
			.stubsMode(StubRunnerProperties.StubsMode.REMOTE);
	// end:rule[]
	//tag::setup[]
	@Before
	public void setupPort() {
		this.beerController.port = this.rule.findStubUrl("beer-api-producer-pact").getPort();
	}
	// end::setup[]
	//remove::end[]

	//tag::tests[]
	@Test public void should_give_me_a_beer_when_im_old_enough() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/beer")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(this.json.write(new Person("marcin", 25)).getJson()))
				.andExpect(status().isOk())
				.andExpect(content().string("THERE YOU GO"));
	}

	@Test public void should_reject_a_beer_when_im_too_young() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/beer")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(this.json.write(new Person("marcin", 10)).getJson()))
				.andExpect(status().isOk())
				.andExpect(content().string("GET LOST"));
	}
	//end::tests[]
}
