package com.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.json.JacksonTester;

/**
 * @author Marcin Grzejszczak
 */
public abstract class AbstractTest {

	public JacksonTester<Person> json;

	@BeforeEach
	public void setup() {
		ObjectMapper objectMappper = new ObjectMapper();
		// Possibly configure the mapper
		JacksonTester.initFields(this, objectMappper);
	}
}
