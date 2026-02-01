package com.example.kafka.producer;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Tag;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.ConfluentKafkaContainer;
import org.testcontainers.utility.DockerImageName;

import tools.jackson.databind.json.JsonMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.verifier.converter.YamlContract;
import org.springframework.cloud.contract.verifier.messaging.MessageVerifierReceiver;
import org.springframework.cloud.contract.verifier.messaging.boot.AutoConfigureMessageVerifier;
import org.springframework.cloud.contract.verifier.messaging.internal.ContractVerifierObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.JsonKafkaHeaderMapper;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

@Tag("kafka-avro")
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = {
		KafkaAvroProducerApplication.class, BaseClass.TestConfig.class })
@AutoConfigureMessageVerifier
@ActiveProfiles("contracts")
class BaseClass {

	@Autowired
	private BookService bookService;

	@Container
	static ConfluentKafkaContainer kafka = new ConfluentKafkaContainer(
			DockerImageName.parse("confluentinc/cp-kafka"));

	@DynamicPropertySource
	static void kafkaProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
	}

	public void publishBookReturned() {
		bookService.bookReturned("978-1234567890", "Contract Testing for Dummies",
				"John Doe");
	}

	@Configuration
	static class TestConfig {

		@Bean
		KafkaMessageVerifier kafkaTemplateMessageVerifier() {
			return new KafkaMessageVerifier();
		}

		// TODO: remove this workaround after merging:
		// https://github.com/spring-cloud/spring-cloud-contract/pull/2405
		@Bean
		@Primary
		ContractVerifierObjectMapper contractVerifierObjectMapper() {
			var json = JsonMapper.builder()
					.addMixIn(SpecificRecordBase.class, AvroIgnoreMixin.class).build();
			return new ContractVerifierObjectMapper(json);
		}

		@JsonIgnoreProperties({ "schema", "specificData", "classSchema", "conversion" })
		interface AvroIgnoreMixin {

		}

	}

	static class KafkaMessageVerifier implements MessageVerifierReceiver<Message<?>> {

		private final Map<String, BlockingQueue<Message<?>>> broker = new ConcurrentHashMap<>();

		@Override
		public Message<?> receive(String destination, long timeout, TimeUnit timeUnit,
				YamlContract contract) {
			try {
				broker.putIfAbsent(destination, new ArrayBlockingQueue<>(1));
				var messageQueue = broker.get(destination);
				return messageQueue.poll(timeout, timeUnit);
			}
			catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}

		@KafkaListener(topics = { "book.returned" })
		public void listen(ConsumerRecord<?, ?> payload,
				@Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
			Map<String, Object> headers = new HashMap<>();
			new JsonKafkaHeaderMapper().toHeaders(payload.headers(), headers);

			broker.putIfAbsent(topic, new ArrayBlockingQueue<>(1));
			var messageQueue = broker.get(topic);
			messageQueue.add(MessageBuilder.createMessage(payload.value(),
					new MessageHeaders(headers)));
		}

		@Override
		public Message receive(String destination, YamlContract contract) {
			return receive(destination, 15, TimeUnit.SECONDS, contract);
		}

	}

}
