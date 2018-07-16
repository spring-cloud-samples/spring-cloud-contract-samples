package com.example;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Contoller used for SC Contract blog entry
 */
@RestController
class CarRentalHistoryController {
	private final SomeService someService;

	CarRentalHistoryController(SomeService someService) {
		this.someService = someService;
	}

	@GetMapping({"/foo", "/bar"})
	String response() {
		return this.someService.callTheDatabase();
	}
}

interface SomeService {
	String callTheDatabase();
}

@Configuration class CarRentalHistoryConfig {
	@Bean SomeService someService() {
		return () -> "HELLO";
	}
}