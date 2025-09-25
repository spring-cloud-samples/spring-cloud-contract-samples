package com.example;

import org.junit.jupiter.api.BeforeEach;
import tools.jackson.databind.json.JsonMapper;

import org.springframework.boot.test.json.JacksonTester;

/**
 * @author Marcin Grzejszczak
 */
public abstract class AbstractTest {

	public JacksonTester<Person> json;

	@BeforeEach
	public void setup() {
		JsonMapper objectMapper = new JsonMapper();
		// Possibly configure the mapper
		JacksonTester.initFields(this, objectMapper);
	}
}
