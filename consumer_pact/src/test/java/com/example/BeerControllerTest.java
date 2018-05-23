package com.example;

import java.util.HashMap;
import java.util.Map;

import au.com.dius.pact.consumer.Pact;
import au.com.dius.pact.consumer.PactProviderRuleMk2;
import au.com.dius.pact.consumer.PactVerification;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.model.RequestResponsePact;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertEquals;

/**
 * @author Marcin Grzejszczak
 */
//@org.junit.Ignore;
public class BeerControllerTest {

	BeerController controller = new BeerController(new RestTemplate());

	@Rule
	public PactProviderRuleMk2 mockProvider =
			new PactProviderRuleMk2("beer-api-producer-pact", "localhost", 8083, this);


	@Pact(consumer="beer-api-consumer-pact")
	public RequestResponsePact beerNotOk(PactDslWithProvider builder) {
		return builder
				.given("")
					.uponReceiving("Represents a successful scenario of getting a beer")
					.path("/check")
					.method("POST")
					.headers("Content-Type", "application/json")
					.body("{\"name\":\"marcin\",\"age\":25}")
					.willRespondWith()
					.status(200)
					.body("{\"status\":\"OK\"}")
					.headers(responseHeaders())
				.given("")
					.uponReceiving("Represents an unsuccessful scenario of getting a beer")
					.path("/check")
					.method("POST")
					.headers("Content-Type", "application/json")
					.body("{\"name\":\"marcin\",\"age\":10}")
					.willRespondWith()
					.status(200)
					.body("{\"status\":\"NOT_OK\"}")
					.headers(responseHeaders())
				.toPact();
	}

	@Test
	@PactVerification
	public void runTestBeer() {
		// OK
		assertEquals(controller.gimmeABeer(new Person("marcin", 25)), "THERE YOU GO");

		// NOT_OK
		assertEquals(controller.gimmeABeer(new Person("marcin", 10)), "GET LOST");
	}

	private Map<String, String> responseHeaders() {
		Map<String, String> map = new HashMap<>();
		map.put("Content-Type", "application/json;charset=UTF-8");
		return map;
	}
}