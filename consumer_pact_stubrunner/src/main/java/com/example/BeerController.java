package com.example;

import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

/**
 * @author Marcin Grzejszczak
 */
@RestController
class BeerController {

	private final RestTemplate restTemplate;

	int port = 8083;

	BeerController(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	@RequestMapping(method = RequestMethod.POST,
			value = "/beer",
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public String gimmeABeer(@RequestBody Person person) {
		ResponseEntity<Response> response = this.restTemplate.exchange(
				RequestEntity
						.post(URI.create("http://localhost:" + this.port + "/check"))
						.contentType(MediaType.APPLICATION_JSON_UTF8)
						.body(person),
				Response.class);
		switch (response.getBody().status) {
		case OK:
			return "THERE YOU GO";
		default:
			return "GET LOST";
		}
	}
}

class Person {
	public String name;
	public int age;

	public Person(String name, int age) {
		this.name = name;
		this.age = age;
	}

	public Person() {
	}
}

class Response {
	public ResponseStatus status;
}

enum ResponseStatus {
	OK, NOT_OK
}