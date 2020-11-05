/*
 * Copyright 2013-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example;

// remove::start[]
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nullable;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.verifier.converter.YamlContract;
import org.springframework.cloud.contract.verifier.messaging.MessageVerifier;
import org.springframework.cloud.contract.verifier.messaging.boot.AutoConfigureMessageVerifier;
import org.springframework.cloud.contract.verifier.messaging.internal.ContractVerifierMessage;
import org.springframework.cloud.contract.verifier.messaging.internal.ContractVerifierMessaging;
import org.springframework.cloud.contract.verifier.messaging.internal.ContractVerifierObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.DefaultKafkaHeaderMapper;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = { TestConfig.class, Application.class })
@Testcontainers
@AutoConfigureMessageVerifier
@ActiveProfiles("test")
// remove::end[]
public abstract class BaseClass {
	// remove::start[]

	@Container static KafkaContainer kafka = new KafkaContainer();

	@DynamicPropertySource
	static void kafkaProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
	}

	@Autowired
	Controller controller;
	// remove::end[]

	public void trigger() {
		// remove::start[]
		this.controller.sendFoo("example");
		// remove::end[]
	}
}

// remove::start[]
@EnableKafka
@Configuration
class TestConfig {

	@Bean
	KafkaMessageVerifier kafkaTemplateMessageVerifier() {
		return new KafkaMessageVerifier();
	}

}

class KafkaMessageVerifier implements MessageVerifier<Message<?>> {

	private static final Logger log = LoggerFactory.getLogger(KafkaMessageVerifier.class);

	private final Map<String, Message> broker = new ConcurrentHashMap<>();

	private final CyclicBarrier cyclicBarrier = new CyclicBarrier(1);

	@Override
	public Message receive(String destination, long timeout, TimeUnit timeUnit, @Nullable YamlContract contract) {
		Message message = message(destination);
		if (message != null) {
			return message;
		}
		try {
			cyclicBarrier.await(timeout, timeUnit);
		} catch (Exception e) {

		}
		return message(destination);
	}

	private Message message(String destination) {
		Message message = broker.get(destination);
		if (message != null) {
			broker.remove(destination);
			log.info("Removed a message from a topic [" + destination + "]");
		}
		return message;
	}

	@KafkaListener(id = "listener", topicPattern = ".*")
	public void listen(ConsumerRecord payload, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
		log.info("Got a message from a topic [" + topic + "]");
		Map<String, Object> headers = new HashMap<>();
		new DefaultKafkaHeaderMapper().toHeaders(payload.headers(), headers);
		broker.put(topic, MessageBuilder.createMessage(payload.value(), new MessageHeaders(headers)));
	}

	@Override
	public Message receive(String destination, YamlContract contract) {
		return receive(destination, 1, TimeUnit.SECONDS, contract);
	}

	@Override
	public void send(Message message, String destination, @Nullable YamlContract contract) {

	}

	@Override
	public void send(Object payload, Map headers, String destination, @Nullable YamlContract contract) {

	}
}
// remove::end[]
