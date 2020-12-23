/*
 * Copyright 2018-2019 the original author or authors.
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
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nullable;
import javax.jms.JMSException;
import javax.jms.Message;

import org.assertj.core.api.BDDAssertions;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.StubTrigger;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.cloud.contract.verifier.converter.YamlContract;
import org.springframework.cloud.contract.verifier.messaging.MessageVerifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = {TestConfig.class, Application.class})
@AutoConfigureStubRunner(ids = "com.example:beer-api-producer-jms-middleware", stubsMode = StubRunnerProperties.StubsMode.LOCAL)
@Testcontainers
@ActiveProfiles("test")
// remove::end[]
public class ApplicationTests {

	// remove::start[]
	@Container
	static GenericContainer ibmMq = new GenericContainer("ibmcom/mq:9.1.2.0")
			.withEnv("LICENSE", "accept")
			.withEnv("MQ_QMGR_NAME", "QM1")
			.withExposedPorts(1414, 9443);

	@DynamicPropertySource
	static void jmsProperties(DynamicPropertyRegistry registry) {
		registry.add("ibm.mq.connName", () -> "localhost(" + ibmMq.getMappedPort(1414) + ")");
	}

	@BeforeAll
	static void setupAll() {
		ibmMq.start();
	}

	@Autowired
	StubTrigger trigger;
	@Autowired
	Application application;

	@Test
	public void contextLoads() {
		this.trigger.trigger("trigger");

		Awaitility.await().untilAsserted(() -> {
			BDDAssertions.then(this.application.storedFoo).isNotNull();
			BDDAssertions.then(this.application.storedFoo.getFoo()).contains("example");
		});
	}
	// remove::end[]
}

// remove::start[]
@Configuration
class TestConfig {

	@Bean
	MessageVerifier<Message> standaloneMessageVerifier(JmsTemplate jmsTemplate) {
		return new MessageVerifier<>() {
			@Override
			public Message receive(String destination, long timeout, TimeUnit timeUnit, @Nullable YamlContract contract) {
				return null;
			}

			@Override
			public Message receive(String destination, YamlContract contract) {
				return null;
			}

			@Override
			public void send(Message message, String destination, @Nullable YamlContract contract) {
			}

			@Override
			public <T> void send(T payload, Map<String, Object> headers, String destination, @Nullable YamlContract contract) {
				jmsTemplate.send(destination, session -> {
					Message message = session
							.createTextMessage(payload.toString());
					headers.forEach((s, o) -> {
						try {
							message.setStringProperty(s, o.toString());
						}
						catch (JMSException e) {
							throw new IllegalStateException(e);
						}
					});
					return message;
				});
			}
		};
	}
}
// remove::end[]
