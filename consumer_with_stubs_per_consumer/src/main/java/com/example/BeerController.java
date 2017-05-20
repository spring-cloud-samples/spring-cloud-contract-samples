package com.example;

import java.net.MalformedURLException;
import java.net.URI;

import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author Marcin Grzejszczak
 */
@RestController
class BeerController {

	private final RestTemplate restTemplate;

	int port = 8090;

	BeerController(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	@RequestMapping(method = RequestMethod.POST,
			value = "/beer",
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public String gimmeABeer(@RequestBody Person person) throws MalformedURLException {
		//remove::start[]
		//tag::impl[]
		ResponseEntity<Response> response = this.restTemplate.exchange(
				RequestEntity
						.post(URI.create("http://localhost:" + port + "/check"))
						.contentType(MediaType.APPLICATION_JSON)
						.body(person),
				Response.class);
		switch (response.getBody().status) {
		case OK:
			return "THERE YOU GO " + message(response.getBody());
		default:
			return "GET LOST " + message(response.getBody());
		}
		//end::impl[]
		//remove::end[return]
	}

	String message(Response response) {
		// if there's a name - we'll be very informal and use the name
		if (response.name != null) {
			return "MY DEAR FRIEND [" + response.name + "]";
		}
		// otherwise we'll be very official and use the surname
		return "MR [" + response.surname + "]";
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
	// foo-service will expect this
	public String name;
	// bar-service will expect that
	public String surname;
}

enum ResponseStatus {
	OK, NOT_OK
}