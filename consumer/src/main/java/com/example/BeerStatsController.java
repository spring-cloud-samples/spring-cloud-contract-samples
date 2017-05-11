package com.example;

import java.net.MalformedURLException;
import java.net.URI;

import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author Marcin Grzejszczak
 */
@RestController
class BeerStatsController {
	private final RestTemplate restTemplate;

	int port = 8090;

	BeerStatsController(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	@RequestMapping(method = RequestMethod.POST,
			value = "/stats",
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public String howManyHaveIDrankAlready(@RequestBody StatsRequest statsRequest) throws MalformedURLException {
		//remove::start[]
		ResponseEntity<StatsResponse> response = this.restTemplate.exchange(
				RequestEntity
						.post(URI.create("http://localhost:" + port + "/stats"))
						.contentType(MediaType.APPLICATION_JSON)
						.body(new StatsRequest(statsRequest.getName())),
				StatsResponse.class);
		return response.getBody().getText() + ". You've drank <" + response.getBody().getQuantity() + "> beers";
		//remove::end[return]
	}
}

class StatsRequest {
	public String name;

	public StatsRequest(String name) {
		this.name = name;
	}

	public StatsRequest() {
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
}

class StatsResponse {
	public int quantity;
	public String text;

	public StatsResponse(int quantity, String text) {
		this.quantity = quantity;
		this.text = text;
	}

	public StatsResponse() {
	}

	public int getQuantity() {
		return this.quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}
}