package com.example;

import java.net.URI;

import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

enum ResponseStatus {

	OK, NOT_OK

}

/**
 * @author Marcin Grzejszczak
 */
// TODO: Consider an example with OAuthRestTemplate
@RestController
class BeerController {

	private static final String TOO_YOUNG_TOKEN = "Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJERmJmZWVpR29ySXMyZ1VUaDA2d2hfZVZKTUc2azU5X0dVZzBrOVRLb05vIn0.eyJqdGkiOiJiMmY2M2U4Yi04N2ExLTRiYzgtYTEwMC0zMWMwNzdmNTFiYTIiLCJleHAiOjIwMzA3ODA2MDIsIm5iZiI6MCwiaWF0IjoxNTU3ODI3MDAyLCJpc3MiOiJodHRwOi8vbG9jYWxob3N0Ojg3NjUvYXV0aC9yZWFsbXMvc3ByaW5nX2Nsb3VkX2NvbnRyYWN0cyIsImF1ZCI6InNwcmluZ19jbG91ZF9jb250cmFjdHMiLCJzdWIiOiI4NGViYjM2MS0wZWZjLTRmOTgtYTlhMS0wZDY3ZDE2N2I2MGQiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJzcHJpbmdfY2xvdWRfY29udHJhY3RzIiwiYXV0aF90aW1lIjowLCJzZXNzaW9uX3N0YXRlIjoiNzJkOWUxN2MtOTIyNC00MGFmLWFkNDQtMWRhNGZmNDhiNzZiIiwiYWNyIjoiMSIsImFsbG93ZWQtb3JpZ2lucyI6WyIqIl0sInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJjdXN0b21lciJdfSwicmVzb3VyY2VfYWNjZXNzIjp7fSwic2NvcGUiOiIiLCJhdWQiOiJvYXV0aDItcmVzb3VyY2UiLCJ1c2VyX25hbWUiOiJ0b28geW91bmciLCJ1c2VyX2RldGFpbHMiOnsiYWdlIjoxNiwidXNlcm5hbWUiOiJ0b28geW91bmcifSwiYXV0aG9yaXRpZXMiOlsiY3VzdG9tZXIiXX0.Ta182t7wEqzjisSPGcGNANyPdJvzL2BeDFuKHc5T7nwAJJvqn6pT9mtNpuMajaFcRfwr1dhCs5KqOlINuRsgfUFm3o7-kyg-DoS-BurwFvBmkiXeQuYHTBEYpBAI6wTtj8b8nMa0Id16-yv1Wkr82lBS4FiQp_sf5etOsYGUNU__0UUuYDiacZlTJ3kn-m7HA1OvYQm8ccAXX9NZ5AqkrVanGf6LolVJFFQ4uigVR3POjR5S-yFyikAj8PRdPp4UrlB_60S6DRxDxZHIBz4um0c_IZGfHuw9MLq3tr_AozoL57mdSzo58l2_oYWW2eAQfN6HyluRUKAKgmWOcT1v2A";

	private static final String OLD_ENOUGH_TOKEN = "Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJERmJmZWVpR29ySXMyZ1VUaDA2d2hfZVZKTUc2azU5X0dVZzBrOVRLb05vIn0.eyJqdGkiOiJlZGY2OGQ2ZS03NjQzLTRjYjgtODU1NS0xOGNkM2M3Njc2ZTIiLCJleHAiOjIwMzA3ODA5MTMsIm5iZiI6MCwiaWF0IjoxNTU3ODI3MzEzLCJpc3MiOiJodHRwOi8vbG9jYWxob3N0Ojg3NjUvYXV0aC9yZWFsbXMvc3ByaW5nX2Nsb3VkX2NvbnRyYWN0cyIsImF1ZCI6InNwcmluZ19jbG91ZF9jb250cmFjdHMiLCJzdWIiOiJjMzNhMzgxNi1lYWExLTRhYWMtYjdhMi1jNDc3NDRkODcwMDAiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJzcHJpbmdfY2xvdWRfY29udHJhY3RzIiwiYXV0aF90aW1lIjowLCJzZXNzaW9uX3N0YXRlIjoiOGM4ZThhMmMtZDQ5ZC00ZTYxLTlkNTYtOTBkZDVjYTQ1ZDNiIiwiYWNyIjoiMSIsImFsbG93ZWQtb3JpZ2lucyI6WyIqIl0sInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJjdXN0b21lciJdfSwicmVzb3VyY2VfYWNjZXNzIjp7fSwic2NvcGUiOiIiLCJhdWQiOiJvYXV0aDItcmVzb3VyY2UiLCJ1c2VyX25hbWUiOiJvbGQgZW5vdWdoIiwidXNlcl9kZXRhaWxzIjp7ImFnZSI6NDIsInVzZXJuYW1lIjoib2xkIGVub3VnaCJ9LCJhdXRob3JpdGllcyI6WyJjdXN0b21lciJdfQ.igcj_dWdpUEr8WPG06yWUde5bluJMujTdefg24R0XQji5EVoEIQV4xT3D0xOtJDOgK0qSCcz5qUi3xsxbvTJp1xD9WXWIl8lFQA0cP4znSdBEYE-Nv9mLgUaF7QfBpL9_hZtYmeNfkvWk6PqOtvh2VlJ1-5esJ5bzUA3s1h0B8wGKWQUOW3-kCV40iX9gb4BIJfxVDnSytzHQO5iRblHpnWYvJJtWJp2Xx91q22xnvQpBqKaF4n3obYae686apMVMpTFgoFwqcNBaBwStImyh9c_kZMZ8ns-eHFcuzmU_ZA9_VNSK2X6vFcG54H3N3Enf8Dz3RX7LM-Q9iziA82COA";

	private final RestTemplate restTemplate;

	int port = 8090;

	BeerController(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/beer", consumes = MediaType.APPLICATION_JSON_VALUE)
	public String gimmeABeer(@RequestBody Person person) {
		//remove::start[]
		// tag::controller[]
		String token = OLD_ENOUGH_TOKEN;
		if (person.getAge() < 21) {
			token = TOO_YOUNG_TOKEN;
		}
		ResponseEntity<Response> response = this.restTemplate.exchange(
				RequestEntity.post(URI.create("http://localhost:" + this.port + "/check"))
						.contentType(MediaType.APPLICATION_JSON_UTF8)
						.header(AUTHORIZATION, token).build(),
				Response.class);
		switch (response.getBody().status) {
		case OK:
			return "THERE YOU GO";
		default:
			return "GET LOST";
		}
		// end::controller[]
		//remove::end[return]
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

	public String getName() {
		return name;
	}

	public int getAge() {
		return age;
	}

}

class Response {

	public ResponseStatus status;

}
