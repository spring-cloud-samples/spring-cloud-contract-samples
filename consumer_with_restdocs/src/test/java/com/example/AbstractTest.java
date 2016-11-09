package com.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;

/**
 * @author Marcin Grzejszczak
 */
public abstract class AbstractTest {

	public JacksonTester<Person> json;

	@Before
	public void setup() throws IOException {
		ObjectMapper objectMappper = new ObjectMapper();
		// Possibly configure the mapper
		JacksonTester.initFields(this, objectMappper);
	}
}
