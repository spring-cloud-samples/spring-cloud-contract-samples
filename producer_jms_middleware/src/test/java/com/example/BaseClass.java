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
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nullable;

import org.junit.jupiter.api.BeforeAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.HostPortWaitStrategy;
import org.testcontainers.containers.wait.strategy.WaitStrategyTarget;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.verifier.converter.YamlContract;
import org.springframework.cloud.contract.verifier.messaging.MessageVerifier;
import org.springframework.cloud.contract.verifier.messaging.boot.AutoConfigureMessageVerifier;
import org.springframework.cloud.contract.verifier.messaging.internal.ContractVerifierMessage;
import org.springframework.cloud.contract.verifier.messaging.internal.ContractVerifierMessaging;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.Message;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = {TestConfig.class, Application.class})
@Testcontainers
@AutoConfigureMessageVerifier
@ActiveProfiles("test")
// remove::end[]
public abstract class BaseClass {

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
	Controller controller;
	// remove::end[]

	public void trigger() {
		// remove::start[]
		this.controller.sendFoo("example");
		// remove::end[]
	}
}

// remove::start[]
@Configuration
class TestConfig {

	@Bean
	JmsMessageVerifier jmsMessageVerifier() {
		return new JmsMessageVerifier();
	}

	@Bean
	ContractVerifierMessaging<Message> jmsContractVerifierMessaging(JmsMessageVerifier messageVerifier) {
		return new ContractVerifierMessaging<>(messageVerifier) {

			@Override
			protected ContractVerifierMessage convert(Message receive) {
				if (receive == null) {
					return null;
				}
				return new ContractVerifierMessage(receive.getPayload(), receive
						.getHeaders());
			}

		};
	}

}

class JmsMessageVerifier implements MessageVerifier<Message> {

	private static final Logger log = LoggerFactory.getLogger(JmsMessageVerifier.class);

	private final LinkedBlockingQueue<Message> queue = new LinkedBlockingQueue<>();

	@Override
	public Message receive(String destination, long timeout, TimeUnit timeUnit, @Nullable YamlContract contract) {
		try {
			return queue.poll(timeout, timeUnit);
		}
		catch (InterruptedException e) {
			throw new IllegalStateException(e);
		}
	}

	@JmsListener(id = "foo", destination = "DEV.QUEUE.1")
	public void listen(Message message) {
		log.info("Got a message! [{}]", message);
		queue.add(message);
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
