package com.example;

import java.util.HashMap;
import java.util.Map;

import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Marcin Grzejszczak
 */
//@org.junit.jupiter.api.Disabled;
@ExtendWith(PactConsumerTestExt.class)
@PactTestFor(providerName = "beer-api-producer-pact", port = "8083")
public class BeerControllerTest {

	BeerController controller = new BeerController(new RestTemplate());

	@Pact(consumer="beer-api-consumer-pact")
	public RequestResponsePact beerNotOk(PactDslWithProvider builder) {
		return builder
				.given("")
					.uponReceiving("Represents a successful scenario of getting a beer")
					.path("/check")
					.method("POST")
					.headers("Content-Type", "application/json;charset=UTF-8")
					.body("{\"name\":\"marcin\",\"age\":25}")
					.willRespondWith()
					.status(200)
					.body("{\"status\":\"OK\"}")
					.headers(responseHeaders())
				.given("")
					.uponReceiving("Represents an unsuccessful scenario of getting a beer")
					.path("/check")
					.method("POST")
					.headers("Content-Type", "application/json;charset=UTF-8")
					.body("{\"name\":\"marcin\",\"age\":10}")
					.willRespondWith()
					.status(200)
					.body("{\"status\":\"NOT_OK\"}")
					.headers(responseHeaders())
				.toPact();
	}

	@Test
	public void runTestBeer() {
		// OK
		assertEquals(this.controller.gimmeABeer(new Person("marcin", 25)), "THERE YOU GO");

		// NOT_OK
		assertEquals(this.controller.gimmeABeer(new Person("marcin", 10)), "GET LOST");
	}

	private Map<String, String> responseHeaders() {
		Map<String, String> map = new HashMap<>();
		map.put("Content-Type", "application/json;charset=UTF-8");
		return map;
	}
}