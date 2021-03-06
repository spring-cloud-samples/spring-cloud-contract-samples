package com.example;

import com.example.model.BeerCheckStatus;
import com.example.model.PersonToCheck;
import com.example.model.Response;
import reactor.core.publisher.Mono;

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
	public Mono<Response> check(@RequestBody PersonToCheck personToCheck) {
		
		if (this.personCheckingService.shouldGetBeer(personToCheck)) {
			return Mono.just(new Response(BeerCheckStatus.OK));
		}
		return Mono.just(new Response(BeerCheckStatus.NOT_OK));
		
	}
	
}

interface PersonCheckingService {
	Boolean shouldGetBeer(PersonToCheck personToCheck);
}