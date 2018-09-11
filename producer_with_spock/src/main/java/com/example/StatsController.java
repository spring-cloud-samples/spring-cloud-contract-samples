package com.example;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Marcin Grzejszczak
 */
@RestController
public class StatsController {
	private final StatsService statsService;

	public StatsController(StatsService statsService) {
		this.statsService = statsService;
	}

	@RequestMapping(value = "/stats",
			method= RequestMethod.POST,
			consumes="application/json",
			produces="application/json")
	public StatsResponse check(@RequestBody StatsRequest request) {
		int bottles = statsService.findBottlesByName(request.getName());
		String text = String.format("Dear %s thanks for your interested in drinking beer", request.getName());
		return new StatsResponse(bottles, text);
	}
}

interface StatsService {
	int findBottlesByName(String name);
}

@Service
class NoOpStatsService implements StatsService {

	@Override public int findBottlesByName(String name) {
		return 0;
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