package com.example;

import java.net.URI;

import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author Marcin Grzejszczak
 */
@RestController
public class GrumpyBartenderController {

	private final RestTemplate restTemplate;
	int port = 8098;

	public GrumpyBartenderController(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	@PostMapping(value = "/grumpy", produces = MediaType.APPLICATION_JSON_VALUE)
	GrumpyResponse grumpy(@RequestBody GrumpyPerson person) {
		//remove::start[]
		//tag::controller[]
		ResponseEntity<GrumpyBartenderResponse> response = this.restTemplate.exchange(
				RequestEntity
						.post(URI.create("http://localhost:" + port + "/buy"))
						.contentType(MediaType.APPLICATION_JSON)
						.body(person),
				GrumpyBartenderResponse.class);
		switch (response.getBody().status) {
		case OK:
			return new GrumpyResponse(response.getBody().message, "Enjoy!");
		default:
			return new GrumpyResponse(response.getBody().message, "Go to another bar");
		}
		//end::controller[]
		//remove::end[return]
	}
}

class GrumpyPerson {
	public String name;
	public Integer age;
}

class GrumpyBartenderResponse {
	public GrumpyBartenderResponseStatus status;
	public String message;
}

enum GrumpyBartenderResponseStatus {
	OK, NOT_OK
}

class GrumpyResponse {
	public String whatTheBartenderSaid;
	public String whatDoWeDo;

	public GrumpyResponse(String whatTheBartenderSaid, String whatDoWeDo) {
		this.whatTheBartenderSaid = whatTheBartenderSaid;
		this.whatDoWeDo = whatDoWeDo;
	}
}