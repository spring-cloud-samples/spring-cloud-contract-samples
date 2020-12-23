package com.example;

import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.Test;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerPort;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.http.ResponseEntity;

/**
 * Client used (with small adaptation) in a SC contract blog post
 *
 * @author Marcin Grzejszczak
 */
//remove::start[]
@SpringBootTest(webEnvironment = WebEnvironment.MOCK,
		properties = {"spring.application.name=foo-consumer"})
@AutoConfigureStubRunner(stubsMode = StubRunnerProperties.StubsMode.LOCAL,
		ids = "com.example:beer-api-producer-with-stubs-per-consumer",
		stubsPerConsumer = true)
//remove::end[]
//@org.junit.jupiter.api.Disabled
public class FooControllerTest {

	//remove::start[]
	@StubRunnerPort("beer-api-producer-with-stubs-per-consumer")
	//remove::end[]
	int producerPort;

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