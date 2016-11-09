package com.example;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.Charset;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Marcin Grzejszczak
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ClientApplication.class, webEnvironment = WebEnvironment.MOCK)
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
// Broken for some reason
//@AutoConfigureWireMock(stubs = "META-INF/com.example/beer-api-producer-restdocs", port = 8090)
@AutoConfigureWireMock(port = 8090)
public class BeerControllerTest extends AbstractTest {

	@Autowired  MockMvc mockMvc;

	@Value("classpath:META-INF/com.example/beer-api-producer-restdocs/0.0.1-SNAPSHOT/mappings/shouldGrantABeerIfOldEnough.json")
	private Resource successful;

	@Value("classpath:META-INF/com.example/beer-api-producer-restdocs/0.0.1-SNAPSHOT/mappings/shouldRejectABeerIfTooYoung.json")
	private Resource rejected;

	@Autowired
	private WireMockServer server;

	@Before
	public void setupWireMock() throws IOException {
		server.addStubMapping(StubMapping.buildFrom(StreamUtils.copyToString(
				successful.getInputStream(), Charset.forName("UTF-8"))));
		server.addStubMapping(StubMapping.buildFrom(StreamUtils.copyToString(
				rejected.getInputStream(), Charset.forName("UTF-8"))));
	}

	@Test public void should_give_me_a_beer_when_im_old_enough() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/beer")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json.write(new Person("marcin", 22)).getJson()))
				.andExpect(status().isOk())
				.andExpect(content().string("THERE YOU GO"));
	}

	@Test public void should_reject_a_beer_when_im_too_young() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/beer")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json.write(new Person("marcin", 17)).getJson()))
				.andExpect(status().isOk())
				.andExpect(content().string("GET LOST"));
	}
}


/*



	@Test public void should_give_me_a_beer_when_im_old_enough() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/beer")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json.write(new Person("marcin", 22)).getJson()))
				.andExpect(status().isOk())
				.andExpect(content().string("THERE YOU GO"));
	}

	@Test public void should_reject_a_beer_when_im_too_young() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/beer")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json.write(new Person("marcin", 17)).getJson()))
				.andExpect(status().isOk())
				.andExpect(content().string("GET LOST"));
	}

 */

