package com.example;

import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.cloud.contract.stubrunner.StubFinder;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerPort;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Marcin Grzejszczak
 */
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
// example of usage with fixed port

@AutoConfigureStubRunner(stubsMode = StubRunnerProperties.StubsMode.LOCAL, ids = "com.example:beer-api-producer-advanced")


@DirtiesContext
public class GrumpyBartenderControllerTest extends AbstractTest {

	@Autowired MockMvc mockMvc;
	@Autowired GrumpyBartenderController controller;
	@Autowired StubFinder stubFinder;

	
	@StubRunnerPort("beer-api-producer-advanced") int producerPort;
	
	@BeforeEach
	public void setupPort() {
		// either one or the other option
		
		int portFromStubFinder = this.stubFinder.findStubUrl("beer-api-producer-advanced").getPort();
		
		int port2 = this.producerPort;
		BDDAssertions.then(portFromStubFinder).isEqualTo(port2);
		this.controller.port = portFromStubFinder;
	}

	
	@Test public void should_fail_to_sell_beer() throws Exception {
		
		this.mockMvc.perform(MockMvcRequestBuilders.post("/grumpy")
				.contentType(MediaType.APPLICATION_JSON)
				.content(this.json.write(new Person("marcin", 22)).getJson()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.whatTheBartenderSaid").value("You're drunk [marcin]. Go home!"))
				.andExpect(jsonPath("$.whatDoWeDo").value("Go to another bar"));
		
	}

	@Test public void should_sell_beer_to_Josh() throws Exception {
		
		this.mockMvc.perform(MockMvcRequestBuilders.post("/grumpy")
				.contentType(MediaType.APPLICATION_JSON)
				.content(this.json.write(new Person("starbuxman", 22)).getJson()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.whatTheBartenderSaid").value("There you go Josh!"))
				.andExpect(jsonPath("$.whatDoWeDo").value("Enjoy!"));
		
	}
	
}