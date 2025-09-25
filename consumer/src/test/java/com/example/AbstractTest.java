package com.example;

import org.junit.jupiter.api.BeforeEach;
import tools.jackson.databind.json.JsonMapper;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;

/**
 * @author Marcin Grzejszczak
 */
// For backward compatibility
@ImportAutoConfiguration(TestChannelBinderConfiguration.class)
public abstract class AbstractTest {

	public JacksonTester<Person> json;
	public JacksonTester<StatsRequest> statsJson;

	@BeforeEach
	public void setup() {
		JsonMapper objectMapper = new JsonMapper();
		// Possibly configure the mapper
		JacksonTester.initFields(this, objectMapper);
	}
}
