package com.example.demo;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.Handler;

import org.springframework.stereotype.Component;

/**
 * @author Marcin Grzejszczak
 */
@Component
public class MyProcessor {

	ObjectMapper objectMapper = new ObjectMapper();

	@Handler
	public Verification handle(String body) throws IOException {
		Person person = this.objectMapper.readValue(body, Person.class);
		return new Verification(person.age >= 20);
	}
}
