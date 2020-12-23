package com.example.intoxication;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerPort;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.example.intoxication.DrunkLevel.DRUNK;
import static com.example.intoxication.DrunkLevel.SOBER;
import static com.example.intoxication.DrunkLevel.TIPSY;
import static com.example.intoxication.DrunkLevel.WASTED;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Marcin Grzejszczak
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
// remove::start[]
@AutoConfigureStubRunner(stubsMode = StubRunnerProperties.StubsMode.LOCAL, ids = "com.example:beer-api-producer-junit4")
// remove::end[]
@DirtiesContext
public class IntoxicationControllerTest {

	@Autowired
	MockMvc mockMvc;

	@Autowired
	IntoxicationController intoxicationController;

	// remove::start[]
	@StubRunnerPort("beer-api-producer-junit4")
	int producerPort;

	private JacksonTester<Person> json;

	@Before
	public void setUp() {
		this.intoxicationController.port = this.producerPort;
		ObjectMapper objectMappper = new ObjectMapper();
		// Possibly configure the mapper
		JacksonTester.initFields(this, objectMappper);
	}
	// remove::end[]

	@Test
	public void should_eventually_get_completely_wasted() throws Exception {
		// remove::start[]
		sendARequestAndExpectStatuses(SOBER, TIPSY);
		sendARequestAndExpectStatuses(TIPSY, DRUNK);
		sendARequestAndExpectStatuses(DRUNK, WASTED);
		// remove::end[]
	}

	private void sendARequestAndExpectStatuses(DrunkLevel previousStatus, DrunkLevel currentStatus) throws Exception {
		// remove::start[]
		// tag::test[]
		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/wasted").contentType(MediaType.APPLICATION_JSON)
						.content(json.write(new Person("marcin")).getJson()))
				.andExpect(status().isOk()).andExpect(content().json("{\"previousStatus\":\"" + previousStatus.name()
						+ "\",\"currentStatus\":\"" + currentStatus.name() + "\"}"));
		// end::test[]
		// remove::end[]
	}

}
