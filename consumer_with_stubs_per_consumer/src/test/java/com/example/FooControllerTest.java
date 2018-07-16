package com.example;

import org.assertj.core.api.BDDAssertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerPort;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.client.RestTemplate;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Client used (with small adaptation) in a SC contract blog post
 *
 * @author Marcin Grzejszczak
 */
@RunWith(SpringRunner.class)
//remove::start[]
@SpringBootTest(webEnvironment = WebEnvironment.MOCK,
		properties = {"spring.application.name=foo-consumer"})
@AutoConfigureStubRunner(stubsMode = StubRunnerProperties.StubsMode.LOCAL,
		ids = "com.example:beer-api-producer-with-stubs-per-consumer",
		stubsPerConsumer = true)
//remove::end[]
//@org.junit.Ignore
public class FooControllerTest {

	@StubRunnerPort("beer-api-producer-with-stubs-per-consumer") int producerPort;

	@Test
	public void should_return_foo_for_foo_consumer() {
		String response = new TestRestTemplate()
				.getForObject("http://localhost:" + this.producerPort + "/foo",
						String.class);

		BDDAssertions.then(response).isEqualTo("OK");
	}

	@Test
	public void should_fail_to_return_bar_for_foo_consumer() {
		ResponseEntity<String> entity = new TestRestTemplate()
				.getForEntity("http://localhost:" + this.producerPort + "/bar",
						String.class);

		BDDAssertions.then(entity.getStatusCodeValue()).isEqualTo(404);
	}
}