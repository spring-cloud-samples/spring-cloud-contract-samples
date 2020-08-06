package com.example.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.CamelContext;
import org.apache.camel.ConsumerTemplate;
import org.apache.camel.ProducerTemplate;
import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest
@AutoConfigureStubRunner(
		ids = "com.example:beer-api-producer-camel",
		stubsMode = StubRunnerProperties.StubsMode.LOCAL
)
// IMPORTANT
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class DemoApplicationTests {

	@Autowired ConsumerTemplate consumerTemplate;
	@Autowired ProducerTemplate producerTemplate;
	@Autowired CamelContext camelContext;
	ObjectMapper objectMapper = new ObjectMapper();

	// consumer -> seda:person
	// 	producers -> seda:person -> person -> verifications -> seda:verifications
	// consumer -> seda:verifications

	@BeforeEach
	public void setup() {
		this.camelContext.getShutdownStrategy().setTimeout(1);
	}

	@Test
	public void should_trigger_a_negative_verification() throws Exception {
		this.producerTemplate.sendBodyAndHeader("seda:person", new Person(17),
				"contentType", "application/json");

		String string =
				this.consumerTemplate.receiveBody("seda:verifications", String.class);
		Verification verification = this.objectMapper.readerFor(Verification.class).readValue(string);
		BDDAssertions.then(verification).isNotNull();
		BDDAssertions.then(verification.eligible).isFalse();
	}

	@Test
	public void should_trigger_a_positive_verification() throws Exception {
		this.producerTemplate.sendBodyAndHeader("seda:person", new Person(25),
				"contentType", "application/json");

		String string =
				this.consumerTemplate.receiveBody("seda:verifications", String.class);
		Verification verification = this.objectMapper.readerFor(Verification.class).readValue(string);
		BDDAssertions.then(verification).isNotNull();
		BDDAssertions.then(verification.eligible).isTrue();
	}

}
