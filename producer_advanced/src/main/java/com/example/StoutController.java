package com.example;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Marcin Grzejszczak
 */
@RestController
class StoutController {

	//remove::start[]
	@GetMapping("/stout")
	String stout() {
		return "STOUT";
	}
	//remove::end[]
}
