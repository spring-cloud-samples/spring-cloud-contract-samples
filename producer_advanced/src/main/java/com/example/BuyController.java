package com.example;

import java.util.concurrent.Callable;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Marcin Grzejszczak
 */
@RestController
class BuyController {

	//remove::start[]
	//tag::impl[]
	@PostMapping(value = "/buy",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	Callable<Response> buy(@RequestBody Person person) {
		if ("starbuxman".equalsIgnoreCase(person.name)) {
			return () -> new Response(Status.OK, "There you go Josh!");
		}
		return () ->new Response(Status.NOT_OK, "You're drunk [" + person.name + "]. Go home!");
	}
	//end::impl[]
	//remove::end[]
}

class Person {
	public String name;
}

class Response {
	public Status status;
	public String message;

	public Response(Status status, String message) {
		this.status = status;
		this.message = message;
	}

	public Response() {
	}
}

enum Status {
	OK, NOT_OK
}
