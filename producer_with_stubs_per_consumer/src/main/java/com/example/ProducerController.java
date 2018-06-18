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
		//tag:impl[]
		if (personCheckingService.shouldGetBeer(personToCheck)) {
			return new Response(BeerCheckStatus.OK, personToCheck.name);
		}
		return new Response(BeerCheckStatus.NOT_OK, personToCheck.name);
		//end:impl[]
		//remove::end[return]
	}
	
}

interface PersonCheckingService {
	Boolean shouldGetBeer(PersonToCheck personToCheck);
}

class PersonToCheck {
	public int age;
	public String name;

	public PersonToCheck(int age) {
		this.age = age;
	}

	public PersonToCheck() {
	}
}

class Response {
	public BeerCheckStatus status;
	public String name;
	public String surname;

	// we're setting the name to both fields
	Response(BeerCheckStatus status, String name) {
		this.status = status;
		this.name = name;
		this.surname = name;
	}
}

enum BeerCheckStatus {
	OK, NOT_OK
}