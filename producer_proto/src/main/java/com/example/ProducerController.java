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
			consumes="application/x-protobuf",
			produces="application/x-protobuf")
	public Beer.Response check(@RequestBody Beer.PersonToCheck personToCheck) {
		//remove::start[]
		if (this.personCheckingService.shouldGetBeer(personToCheck)) {
			return Beer.Response.newBuilder().setStatus(Beer.Response.BeerCheckStatus.OK).build();
		}
		return Beer.Response.newBuilder().setStatus(Beer.Response.BeerCheckStatus.NOT_OK).build();
		//remove::end[return]
	}
	
}

interface PersonCheckingService {
	Boolean shouldGetBeer(Beer.PersonToCheck personToCheck);
}