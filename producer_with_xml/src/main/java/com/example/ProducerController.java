package com.example;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProducerController {

	@RequestMapping(value = "/order",
			method = RequestMethod.POST,
			consumes = "application/xml",
			produces = "application/xml")
	public BeerOrderResponse sell(@RequestBody BeerOrder beerOrder) {
		return new BeerOrderResponse(UUID.randomUUID().toString(),
				beerOrder.beerName);
	}

	@RequestMapping(value = "/cancelOrder",
			method = RequestMethod.POST,
			consumes = "application/xml",
			produces = "application/xml")
	public BeerOrderResponse cancelOrder(@RequestBody BeerOrder beerOrder) {
		return new BeerOrderResponse("uuid",
				beerOrder.beerName);
	}
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

	BeerOrderResponse(String transactionUuid, List<String> beerName) {
		this.transactionUuid = transactionUuid;
		this.beerName = beerName;
	}
}