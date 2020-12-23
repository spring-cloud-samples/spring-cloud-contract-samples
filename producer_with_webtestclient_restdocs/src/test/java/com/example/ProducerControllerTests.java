package com.example;

import com.example.model.PersonToCheck;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
//remove::start[]
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
//remove::end[]
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
//remove::start[]
import org.springframework.cloud.contract.wiremock.restdocs.SpringCloudContractRestDocs;
import org.springframework.cloud.contract.wiremock.restdocs.WireMockWebTestClient;
//remove::end[]
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
//remove::start[]
import org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation;
//remove::end[]
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * @author Marcin Grzejszczak
 */
@SpringBootTest(classes = ProducerControllerTests.Config.class)
//remove::start[]
@AutoConfigureRestDocs(outputDir = "target/snippets")
//remove::end[]
@AutoConfigureWebTestClient
@AutoConfigureJsonTesters
@DirtiesContext
public class ProducerControllerTests {

	@Autowired private WebTestClient webTestClient;

	private JacksonTester<PersonToCheck> json;

	@BeforeEach
	public void setup() {
		ObjectMapper objectMappper = new ObjectMapper();
		// Possibly configure the mapper
		JacksonTester.initFields(this, objectMappper);
	}

	@Test
	public void should_grant_a_beer_when_person_is_old_enough() throws Exception {
		PersonToCheck personToCheck = new PersonToCheck(34);
		//remove::start[]
		this.webTestClient.post().uri("/check")
				.contentType(MediaType.APPLICATION_JSON)
				.syncBody(this.json.write(personToCheck).getJson())
				.exchange()
				.expectBody().jsonPath("$.status").value(Matchers.equalTo("OK"))
				.consumeWith(WireMockWebTestClient.verify()
						.jsonPath("$[?(@.age >= 20)]")
						.contentType(MediaType.valueOf("application/json"))
						.stub("shouldGrantABeerIfOldEnough"))
				.consumeWith(WebTestClientRestDocumentation.document("shouldGrantABeerIfOldEnough",
						SpringCloudContractRestDocs.dslContract()));
		//remove::end[]
	}

	@Test
	public void should_reject_a_beer_when_person_is_too_young() throws Exception {
		PersonToCheck personToCheck = new PersonToCheck(10);
		//remove::start[]
		this.webTestClient.post().uri("/check")
				.contentType(MediaType.APPLICATION_JSON)
				.syncBody(this.json.write(personToCheck).getJson())
				.exchange()
				.expectBody().jsonPath("$.status").value(Matchers.equalTo("NOT_OK"))
				.consumeWith(WireMockWebTestClient.verify()
						.jsonPath("$[?(@.age < 20)]")
						.contentType(MediaType.valueOf("application/json"))
						.stub("shouldRejectABeerIfTooYoung"))
				.consumeWith(WebTestClientRestDocumentation.document("shouldRejectABeerIfTooYoung",
						SpringCloudContractRestDocs.dslContract()));
		//remove::end[]
	}

	@Configuration
	@EnableAutoConfiguration
	static class Config {
		//remove:start[]
		@Bean
		PersonCheckingService personCheckingService() {
			return personToCheck -> personToCheck.age >= 20;
		}
		//remove::end[]

		@Bean
		ProducerController producerController(PersonCheckingService service) {
			return new ProducerController(service);
		}
	}
}
