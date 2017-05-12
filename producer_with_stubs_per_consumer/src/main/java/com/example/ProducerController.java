package com.example;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProducerController {

	private final PersonCheckingService personCheckingService;

	public ProducerController(PersonCheckingService personCheckingService) {
		this.personCheckingService = personCheckingService;
	}

	@RequestMapping(value = "/check",
			method=RequestMethod.POST,
			consumes="application/json",
			produces="application/json")
	public Response check(@RequestBody PersonToCheck personToCheck) {
		//remove::start[]
		if (personCheckingService.shouldGetBeer(personToCheck)) {
			return new Response(BeerCheckStatus.OK, "foo", "bar");
		}
		return new Response(BeerCheckStatus.NOT_OK, "foo", "bar");
		//remove::end[return]
	}
	
}

interface PersonCheckingService {
	Boolean shouldGetBeer(PersonToCheck personToCheck);
}

class PersonToCheck {
	//remove::start[]
	public int age;

	public PersonToCheck(int age) {
		this.age = age;
	}

	public PersonToCheck() {
	}
	//remove::end[]
}

class Response {
	public BeerCheckStatus status;
	public String foo;
	public String bar;
	
	Response(BeerCheckStatus status, String foo, String bar) {
		this.status = status;
		this.foo = foo;
		this.bar = bar;
	}
}

enum BeerCheckStatus {
	OK, NOT_OK
}