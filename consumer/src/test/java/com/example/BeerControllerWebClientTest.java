package com.example;

import java.util.Objects;

import org.junit.jupiter.api.Test;

import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerPort;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * @author Marcin Grzejszczak
 */
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
//remove::start[]
@AutoConfigureStubRunner(stubsMode = StubRunnerProperties.StubsMode.LOCAL, ids = "com.example:beer-api-producer-webflux")
//remove::end[]
@DirtiesContext
//@org.junit.jupiter.api.Disabled
public class BeerControllerWebClientTest extends AbstractTest {

	//remove::start[]
	@StubRunnerPort("beer-api-producer-webflux") int producerPort;

	//remove::end[]
	@Test public void should_give_me_a_beer_when_im_old_enough() throws Exception {
		//remove::start[]
		WebTestClient.bindToServer()
				.build()
				.post()
				.uri("http://localhost:" + this.producerPort + "/check")
				.syncBody(new WebClientPerson("marcin", 22))
				.header("Content-Type", "application/json")
				.exchange()
				.expectStatus().is2xxSuccessful()
				.expectBody(WebClientResponse.class)
				.isEqualTo(new WebClientResponse(WebClientResponseStatus.OK));
		//remove::end[]
	}

	@Test public void should_reject_a_beer_when_im_too_young() throws Exception {
		//remove::start[]
		WebTestClient.bindToServer()
				.build()
				.post()
				.uri("http://localhost:" + this.producerPort + "/check")
				.syncBody(new WebClientPerson("marcin", 17))
				.header("Content-Type", "application/json")
				.exchange()
				.expectStatus().is2xxSuccessful()
				.expectBody(WebClientResponse.class)
				.isEqualTo(new WebClientResponse(WebClientResponseStatus.NOT_OK));
		//remove::end[]
	}
}


class WebClientPerson {
	public String name;
	public int age;

	public WebClientPerson(String name, int age) {
		this.name = name;
		this.age = age;
	}

	public WebClientPerson() {
	}
}

class WebClientResponse {
	public WebClientResponseStatus status;

	WebClientResponse(WebClientResponseStatus status) {
		this.status = status;
	}

	WebClientResponse() {
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		WebClientResponse that = (WebClientResponse) o;
		return this.status == that.status;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.status);
	}
}

enum WebClientResponseStatus {
	OK, NOT_OK
}
