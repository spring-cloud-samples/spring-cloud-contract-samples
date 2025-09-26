package com.example.demo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.support.DefaultExchange;
import org.apache.camel.support.DefaultMessage;
import org.apache.camel.test.spring.junit5.CamelSpringBootTest;

import org.springframework.beans.factory.annotation.Autowired;
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

	@Autowired
	CamelContext context;

	ObjectMapper objectMapper = new ObjectMapper(); // TODO: For sure serializing stuff in Camel can be done easier

	public void triggerMessage(int age) throws JsonProcessingException {
		Exchange exchange = new DefaultExchange(context);
		Message message = new DefaultMessage(exchange);
		message.setHeader("contentType", "application/json");
		message.setBody(objectMapper.writeValueAsString(new Person(age)));
		exchange.setMessage(message);
		context.createProducerTemplate().send("seda:person", exchange);
	}

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
