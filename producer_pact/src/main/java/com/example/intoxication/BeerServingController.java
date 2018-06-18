package com.example.intoxication;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Scenario based controller
 */
@RestController
public class BeerServingController {

	private final ResponseProvider responseProvider;

	public BeerServingController(ResponseProvider responseProvider) {
		this.responseProvider = responseProvider;
	}

	@RequestMapping(value = "/beer",
			method=RequestMethod.POST,
			consumes="application/json",
			produces="application/json")
	public Response check(@RequestBody Customer customer) {
		//remove::start[]
		return this.responseProvider.thereYouGo(customer);
		//remove::end[return]
	}
	
}

interface ResponseProvider {
	Response thereYouGo(Customer personToCheck);
}

class Customer {
	//remove::start[]
	public String name;

	public Customer(String name) {
		this.name = name;
	}

	public Customer() {
	}
	//remove::end[]
}

class Response {
	public DrunkLevel previousStatus;
	public DrunkLevel currentStatus;

	public Response(DrunkLevel previousStatus, DrunkLevel currentStatus) {
		this.previousStatus = previousStatus;
		this.currentStatus = currentStatus;
	}
}

enum DrunkLevel {
	SOBER, TIPSY, DRUNK, WASTED
}