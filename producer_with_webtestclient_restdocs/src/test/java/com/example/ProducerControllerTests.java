package com.example;

import com.example.model.PersonToCheck;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;

import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.cloud.contract.wiremock.restdocs.SpringCloudContractRestDocs;
import org.springframework.cloud.contract.wiremock.restdocs.WireMockSnippet;
import org.springframework.cloud.contract.wiremock.restdocs.WireMockWebTestClient;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation;

import org.springframework.restdocs.webtestclient.WebTestClientRestDocumentationConfigurer;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * @author Marcin Grzejszczak
 */
@SpringBootTest(classes = ProducerControllerTests.Config.class)
@AutoConfigureRestDocs(outputDir = "target/snippets")
// @AutoConfigureWebTestClient
@DirtiesContext
@ExtendWith(RestDocumentationExtension.class)
public class ProducerControllerTests {

	WebTestClient webTestClient;

	private JacksonTester<PersonToCheck> json;

	@Autowired ProducerController producerController;

	@BeforeEach
	public void setup(RestDocumentationContextProvider restDocumentation) {
		this.webTestClient = WebTestClient.bindToController(producerController)
				.configureClient()
				.filter(WebTestClientRestDocumentation.documentationConfiguration(restDocumentation))
				.build();
		ObjectMapper objectMappper = new ObjectMapper();
		// Possibly configure the mapper
		JacksonTester.initFields(this, objectMappper);
	}

	@Test
	public void should_grant_a_beer_when_person_is_old_enough() throws Exception {
		PersonToCheck personToCheck = new PersonToCheck(34);
		
		this.webTestClient.post().uri("/check")
				.contentType(MediaType.APPLICATION_JSON)
				.syncBody(this.json.write(personToCheck).getJson())
				.exchange()
				.expectBody().jsonPath("$.status").value(Matchers.equalTo("OK"))
				.consumeWith(WireMockWebTestClient.verify()
						.jsonPath("$[?(@.age >= 20)]")
						.contentType(MediaType.valueOf("application/json")))
				.consumeWith(WebTestClientRestDocumentation.document("shouldGrantABeerIfOldEnough",
						SpringCloudContractRestDocs.dslContract(), new WireMockSnippet()));
		
	}

	@Test
	public void should_reject_a_beer_when_person_is_too_young() throws Exception {
		PersonToCheck personToCheck = new PersonToCheck(10);
		
		this.webTestClient.post().uri("/check")
				.contentType(MediaType.APPLICATION_JSON)
				.syncBody(this.json.write(personToCheck).getJson())
				.exchange()
				.expectBody().jsonPath("$.status").value(Matchers.equalTo("NOT_OK"))
				.consumeWith(WireMockWebTestClient.verify()
						.jsonPath("$[?(@.age < 20)]")
						.contentType(MediaType.valueOf("application/json")))
				.consumeWith(WebTestClientRestDocumentation.document("shouldRejectABeerIfTooYoung",
						SpringCloudContractRestDocs.dslContract(), new WireMockSnippet()));
		
	}

	@Configuration
	@EnableAutoConfiguration
	static class Config {
		//remove:start[]
		@Bean
		PersonCheckingService personCheckingService() {
			return personToCheck -> personToCheck.age >= 20;
		}
		

		@Bean
		ProducerController producerController(PersonCheckingService service) {
			return new ProducerController(service);
		}
	}
}
