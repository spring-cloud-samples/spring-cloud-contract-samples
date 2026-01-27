package com.example.kafka.consumer;

import java.util.HashMap;
import java.util.Map;

import com.example.kafka.avro.Book;

import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.ConfluentKafkaContainer;
import org.testcontainers.utility.DockerImageName;

import wiremock.com.fasterxml.jackson.core.JsonProcessingException;
import wiremock.com.fasterxml.jackson.databind.json.JsonMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.cloud.contract.stubrunner.StubTrigger;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.cloud.contract.verifier.converter.YamlContract;
import org.springframework.cloud.contract.verifier.messaging.MessageVerifierSender;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static java.util.Collections.emptyMap;
import static org.awaitility.Awaitility.await;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.verify;

@Tag("kafka-avro")
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {
		CollaborationTest.TestConfig.class, KafkaAvroConsumerApplication.class })
@AutoConfigureStubRunner(ids = "org.springframework.cloud:spring-cloud-contract-sample-kafka-avro-producer:+:stubs", stubsMode = StubRunnerProperties.StubsMode.LOCAL)
@ExtendWith(OutputCaptureExtension.class)
class CollaborationTest {

	@Autowired
	StubTrigger trigger;

	@MockitoBean
	EmailService emailService;

	@Container
	static ConfluentKafkaContainer kafka = new ConfluentKafkaContainer(
			DockerImageName.parse("confluentinc/cp-kafka"));

	@DynamicPropertySource
	static void kafkaProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
	}

	@Test
	void shouldSendEmail_onBookReturned() {
		trigger.trigger("book_returned");

		// @formatter:off
		await().untilAsserted(() ->
			verify(emailService).sendEmail(
				contains("Title: Contract Testing for Dummies, Author: John Doe, ISBN: 978-1234567890")));
		// @formatter:om
	}

	@Configuration
	static class TestConfig {

		@Bean
		MessageVerifierSender<Message<?>> standaloneMessageVerifier(KafkaTemplate<String, Object> kafkaTemplate) {
			return new KafkaAvroMessageVerifierSender<>(kafkaTemplate);
		}

	}

	static class KafkaAvroMessageVerifierSender<M> implements MessageVerifierSender<M> {

		private final KafkaTemplate<String, Object> kafkaTemplate;

		// TODO: should this be the default?
		@Override
		public void send(M message, String destination, @Nullable YamlContract contract) {
			send(message, emptyMap(), destination, contract);
		}

		@Override
		public <T> void send(T payload, Map<String, Object> headers, String destination,
				@Nullable YamlContract contract) {
			Map<String, Object> newHeaders = headers != null ? new HashMap<>(headers) : new HashMap<>();
			newHeaders.put(KafkaHeaders.TOPIC, destination);
			MessageHeaders msgHeaders = new MessageHeaders(newHeaders);

			try {
				// TODO: remove this workaround after merging:
				// https://github.com/spring-cloud/spring-cloud-contract/issues/2404
				Book avroPayload = new JsonMapper().readValue(payload.toString(), Book.class);
				var message = MessageBuilder.createMessage(avroPayload, msgHeaders);
				kafkaTemplate.send(message);
			}
			catch (JsonProcessingException e) {
				throw new RuntimeException(e);
			}
		}

		KafkaAvroMessageVerifierSender(KafkaTemplate<String, Object> kafkaTemplate) {
			this.kafkaTemplate = kafkaTemplate;
		}

	}

}
