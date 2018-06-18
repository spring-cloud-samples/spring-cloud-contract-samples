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
