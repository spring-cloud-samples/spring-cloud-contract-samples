package com.example.demo;

import org.apache.camel.test.spring.junit5.CamelSpringBootTest;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cloud.contract.verifier.messaging.boot.AutoConfigureMessageVerifier;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

@CamelSpringBootTest
@ContextConfiguration(classes = BaseClass.MyTestConfiguration.class)
// IMPORTANT
@AutoConfigureMessageVerifier
// IMPORTANT
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public abstract class BaseClass {

	@TestConfiguration
	@EnableAutoConfiguration
	static class MyTestConfiguration extends RouteConfiguration {

		// was:     rabbit
		// will be: a queue
		@Override
		String start() {
			return "seda:person";
		}

		@Override
		String finish() {
			return "seda:verifications";
		}
	}
}