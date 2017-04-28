package com.example;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
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

import static com.example.DrunkLevel.DRUNK;
import static com.example.DrunkLevel.SOBER;
import static com.example.DrunkLevel.TIPSY;
import static com.example.DrunkLevel.WASTED;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Marcin Grzejszczak
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@AutoConfigureStubRunner(workOffline = true, ids = "com.example:beer-api-producer-with-scenario:+:stubs:8090")
@DirtiesContext
public class BeerControllerTest extends AbstractTest {

	@Autowired MockMvc mockMvc;
	@Autowired BeerController beerController;

	@Before
	public void setupPort() {
		beerController.port = 8090;
	}

	@Test public void should_eventually_get_completely_wasted() throws Exception {
		sendARequestAndExpectStatuses(SOBER, TIPSY);
		sendARequestAndExpectStatuses(TIPSY, DRUNK);
		sendARequestAndExpectStatuses(DRUNK, WASTED);
	}

	private void sendARequestAndExpectStatuses(DrunkLevel previousStatus, DrunkLevel currentStatus) throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/wasted")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json.write(new Person("marcin")).getJson()))
				.andExpect(status().isOk())
				.andExpect(content().json("{\"previousStatus\":\"" + previousStatus.name() +
						"\",\"currentStatus\":\"" + currentStatus.name() + "\"}"));
	}
}


