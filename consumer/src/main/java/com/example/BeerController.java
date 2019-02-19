package com.example;

import java.math.BigDecimal;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import static org.apache.logging.log4j.util.Strings.EMPTY;
import static org.springframework.http.MediaType.APPLICATION_XML;

/**
 * @author Marcin Grzejszczak
 */
@RestController
class BeerController {

	private final RestTemplate restTemplate;

	int port = 8090;

	BeerController(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	@RequestMapping(method = RequestMethod.POST,
			value = "/beer",
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public String gimmeABeer(@RequestBody Person person) {
		//remove::start[]
		//tag::controller[]
		ResponseEntity<Response> response = this.restTemplate.exchange(
				RequestEntity
						.post(URI.create("http://localhost:" + this.port + "/check"))
						.contentType(MediaType.APPLICATION_JSON)
						.body(person),
				Response.class);
		switch (response.getBody().status) {
		case OK:
			return "THERE YOU GO";
		default:
			return "GET LOST";
		}
		//end::controller[]
		//remove::end[return]
	}

	@RequestMapping(method = RequestMethod.POST,
			value = "/order",
			consumes = MediaType.APPLICATION_XML_VALUE)
	public String order(@RequestBody BeerOrder beerSaleOrder) {
		ResponseEntity<BeerOrderResponse> response = this.restTemplate.exchange(
				RequestEntity
						.post(URI.create("http://localhost:" + this.port + "/order"))
						.contentType(APPLICATION_XML)
						.body(new BeerOrder(beerSaleOrder.value, beerSaleOrder.beerName)),
				BeerOrderResponse.class);
		return response.getBody() != null ? response
				.getBody().transactionUuid : EMPTY;
	}

	@RequestMapping(method = RequestMethod.POST,
			value = "/cancelOrder",
			consumes = MediaType.APPLICATION_XML_VALUE)
	public String cancelOrder(@RequestBody BeerOrder beerSaleOrder) {
		ResponseEntity<BeerOrderResponse> response = this.restTemplate.exchange(
				RequestEntity
						.post(URI.create("http://localhost:" + this.port + "/cancelOrder"))
						.contentType(APPLICATION_XML)
						.body(new BeerOrder(beerSaleOrder.value, beerSaleOrder.beerName)),
				BeerOrderResponse.class);
		return response.getBody() != null ? response
				.getBody().transactionUuid : EMPTY;
	}
}

class Person {
	public String name;
	public int age;

	public Person(String name, int age) {
		this.name = name;
		this.age = age;
	}

	public Person() {
	}
}

class Response {
	public ResponseStatus status;
}

enum ResponseStatus {
	OK, NOT_OK
}

@JacksonXmlRootElement(localName = "order")
class BeerOrder {

	public BigDecimal value;

	@JacksonXmlElementWrapper(localName = "beerNames")
	public List<String> beerName = new ArrayList<>();

	public BeerOrder() {
	}

	BeerOrder(BigDecimal value, List<String> beerNames) {
		this.value = value;
		this.beerName = beerNames;
	}
}

@JacksonXmlRootElement(localName = "sale")
class BeerOrderResponse {

	public String transactionUuid;

	@JacksonXmlElementWrapper(localName = "beerNames")
	public List<String> beerName;

	BeerOrderResponse() {
	}

	BeerOrderResponse(String transactionUuid, List<String> beerName) {
		this.transactionUuid = transactionUuid;
		this.beerName = beerName;
	}
}

//remove::start[]
/*

	@RequestMapping(method = RequestMethod.POST,
			value = "/beer",
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public String gimmeABeer(@RequestBody Person person) throws MalformedURLException {
		ResponseEntity<Response> response = this.restTemplate.exchange(
				RequestEntity
						.post(URI.create("http://localhost:8090/check"))
						.contentType(MediaType.APPLICATION_JSON)
						.body(person),
				Response.class);
		switch (response.getBody().status) {
		case OK:
			return "THERE YOU GO";
		default:
			return "GET LOST";
		}
	}


 */
//remove::end[]