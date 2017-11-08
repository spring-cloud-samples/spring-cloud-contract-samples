package com.example;

import java.net.URI;

import org.assertj.core.api.BDDAssertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.client.RestTemplate;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Marcin Grzejszczak
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.NONE,
		// NAME IS IMPORTANT
		properties = {"spring.application.name=foo-consumer"})
//@AutoConfigureStubRunner(workOffline = true,
//		ids = "com.example:beer-api-producer-with-stubs-per-consumer:+:stubs:9090",
//		stubsPerConsumer = true)
@DirtiesContext
public class BeerControllerTest {

	@Test
	public void should_give_me_a_beer_when_im_old_enough() throws Exception {
		// given a client is old enough

		// when a request is sent

		// then the response should be OK
	}

	@Test
	public void should_reject_a_beer_when_im_too_young() throws Exception {// given a client is old enough
		// given a client is too young enough

		// when a request is sent

		// then the response should be NOT OK
	}
}








//	@Test
//	public void should_give_me_a_beer_when_im_old_enough() throws Exception {
//		// given a client is old enough
//		RequestEntity<Person> clientIsOldEnough = RequestEntity
//				.post(URI.create("http://localhost:9090/check"))
//				.contentType(MediaType.APPLICATION_JSON)
//				.body(new Person("marcin", 22));
//
//		// when a request is sent
//		ResponseEntity<Response> response = new RestTemplate().exchange(
//				clientIsOldEnough, Response.class);
//
//		// then the response should be OK
//		BDDAssertions.then(response.getBody().status).isEqualTo(ResponseStatus.OK);
//	}
//
//	@Test
//	public void should_reject_a_beer_when_im_too_young() throws Exception {// given a client is old enough
//		// given a client is too young enough
//		RequestEntity<Person> clientIsOldEnough = RequestEntity
//				.post(URI.create("http://localhost:9090/check"))
//				.contentType(MediaType.APPLICATION_JSON)
//				.body(new Person("marcin", 17));
//
//		// when a request is sent
//		ResponseEntity<Response> response = new RestTemplate().exchange(
//				clientIsOldEnough, Response.class);
//
//		// then the response should be NOT OK
//		BDDAssertions.then(response.getBody().status).isEqualTo(ResponseStatus.NOT_OK);
//	}

class Person {
	public String name;
	public Integer age;

	public Person(String name, Integer age) {
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