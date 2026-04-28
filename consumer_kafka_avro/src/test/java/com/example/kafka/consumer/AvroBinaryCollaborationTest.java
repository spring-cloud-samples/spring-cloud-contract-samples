package com.example.kafka.consumer;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import com.example.kafka.avro.Book;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.springframework.boot.kafka.autoconfigure.KafkaProperties;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.ConfluentKafkaContainer;
import org.testcontainers.utility.DockerImageName;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.cloud.contract.stubrunner.StubTrigger;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.cloud.contract.verifier.converter.YamlContract;
import org.springframework.cloud.contract.verifier.messaging.MessageVerifierSender;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
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
		AvroBinaryCollaborationTest.TestConfig.class, KafkaAvroConsumerApplication.class })
@AutoConfigureStubRunner(ids = "org.springframework.cloud:producer_kafka_avro:+:stubs", stubsMode = StubRunnerProperties.StubsMode.LOCAL)
@ExtendWith(OutputCaptureExtension.class)
class AvroBinaryCollaborationTest {

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
		trigger.trigger("book_returned_binary");

		// @formatter:off
		await().untilAsserted(() ->
			verify(emailService).sendEmail(
				contains("Title: Contract Testing for Dummies, Author: John Doe, ISBN: 978-1234567890")));
		// @formatter:on
	}

	@Configuration
	static class TestConfig {

		@Autowired
		private KafkaProperties kafkaProperties;

		/**
		 * Pre-registers the {@link Book} Avro schema in the in-memory mock schema registry.
		 *
		 * <p>Confluent's Avro wire format prefixes every message with a 5-byte header:
		 * a magic byte ({@code 0x00}) followed by a 4-byte schema ID. The consumer's
		 * {@code KafkaAvroDeserializer} reads that ID and fetches the matching schema from
		 * the registry to decode the rest of the bytes.
		 *
		 * <p>The {@code .bin} fixture was produced by
		 * {@code com.example.kafka.producer.AvroBinaryTestFixture} in a
		 * separate JVM run, so the schema it embedded (ID 1) no longer exists in this JVM's
		 * mock registry. Serializing a dummy {@link Book} here forces the serializer to
		 * register the schema, which assigns it ID 1 — the same ID the deserializer will
		 * look up when the fixture bytes arrive on the topic.
		 */
		@PostConstruct
		void registerBookSchema() {
			try (var serializer = new KafkaAvroSerializer()) {
				serializer.configure(kafkaProperties.buildProducerProperties(), false);
				serializer.serialize("book.returned", Book.newBuilder()
						.setIsbn("").setTitle("").setAuthor("").build());
			}
		}

		@Bean
		MessageVerifierSender<Message<?>> standaloneMessageVerifier() {
			Map<String, Object> producerProps = new HashMap<>(kafkaProperties.buildProducerProperties());
			producerProps.put(org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
					org.apache.kafka.common.serialization.ByteArraySerializer.class);
			var factory = new DefaultKafkaProducerFactory<String, byte[]>(producerProps);
			return new AvroBinaryMessageVerifierSender<>(new KafkaTemplate<>(factory));
		}

	}

	static class AvroBinaryMessageVerifierSender<M> implements MessageVerifierSender<M> {

		private final KafkaTemplate<String, byte[]> kafkaTemplate;

		@Override
		public void send(M message, String destination, YamlContract contract) {
			send(message, emptyMap(), destination, contract);
		}

		@Override
		public <T> void send(T payload, Map<String, Object> headers, String destination,
				YamlContract contract) {
			Map<String, Object> newHeaders = headers != null ? new HashMap<>(headers) : new HashMap<>();
			newHeaders.put(KafkaHeaders.TOPIC, destination);
			MessageHeaders msgHeaders = new MessageHeaders(newHeaders);

			// payload is already Confluent wire-format bytes from the .bin fixture —
			// send raw so the consumer's KafkaAvroDeserializer can decode them directly
			var message = MessageBuilder.createMessage((byte[]) payload, msgHeaders);
			kafkaTemplate.send(message);
		}

		AvroBinaryMessageVerifierSender(KafkaTemplate<String, byte[]> kafkaTemplate) {
			this.kafkaTemplate = kafkaTemplate;
		}

	}

}
