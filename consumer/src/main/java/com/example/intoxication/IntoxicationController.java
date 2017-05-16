package com.example.intoxication;

import java.net.MalformedURLException;
import java.net.URI;

import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author Marcin Grzejszczak
 */
@RestController
class IntoxicationController {

	private final RestTemplate restTemplate;

	int port = 8090;

	IntoxicationController(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	@RequestMapping(method = RequestMethod.POST,
			value = "/wasted",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public Response gimmeABeer(@RequestBody Person person) throws MalformedURLException {
		//remove::start[]
		return this.restTemplate.exchange(
				RequestEntity
						.post(URI.create("http://localhost:" + port + "/beer"))
						.contentType(MediaType.APPLICATION_JSON)
						.body(person),
				Response.class).getBody();
		//remove::end[return]
	}
}

class Person {
	public String name;

	public Person(String name) {
		this.name = name;
	}

	public Person() {
	}
}

class Response {
	public DrunkLevel previousStatus;
	public DrunkLevel currentStatus;

	public Response(DrunkLevel previousStatus, DrunkLevel currentStatus) {
		this.previousStatus = previousStatus;
		this.currentStatus = currentStatus;
	}

	public Response() {
	}
}

enum DrunkLevel {
	SOBER, TIPSY, DRUNK, WASTED
}