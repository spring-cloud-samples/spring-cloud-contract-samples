package com.example.demo;

import org.apache.camel.test.spring.CamelSpringRunner;
import org.junit.runner.RunWith;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.verifier.messaging.boot.AutoConfigureMessageVerifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.annotation.DirtiesContext;

@RunWith(CamelSpringRunner.class)
@SpringBootTest(classes = BaseClass.TestConfiguration.class)
// IMPORTANT
@AutoConfigureMessageVerifier
// IMPORTANT
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public abstract class BaseClass {

	@Configuration
	@EnableAutoConfiguration
	static class TestConfiguration extends RouteConfiguration {

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