package com.example;

import reactor.core.publisher.Mono;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class ProducerConfiguration {

	private final PersonCheckingService personCheckingService;

	public ProducerConfiguration(PersonCheckingService personCheckingService) {
		this.personCheckingService = personCheckingService;
	}

	// remove::start[]
    @Bean
    public RouterFunction<ServerResponse> route() {
		return RouterFunctions.route(RequestPredicates.POST("/check")
			.and(RequestPredicates.contentType(MediaType.APPLICATION_JSON)), 
			request -> this.check(request));
    }

	private Mono<ServerResponse> check(ServerRequest request) {
		return request.bodyToMono(PersonToCheck.class)
		.flatMap(person -> {
			if (this.personCheckingService.shouldGetBeer(person)) {
				return Mono.just(new Response(BeerCheckStatus.OK));
			}
			return Mono.just(new Response(BeerCheckStatus.NOT_OK));
		})
		.flatMap(res -> ServerResponse.ok().bodyValue(res));
	}
	// remove::end[]
	
}

interface PersonCheckingService {
	Boolean shouldGetBeer(PersonToCheck personToCheck);
}

class PersonToCheck {
	public int age;

	public PersonToCheck(int age) {
		this.age = age;
	}

	public PersonToCheck() {
	}
}

class Response {
	public BeerCheckStatus status;
	
	Response(BeerCheckStatus status) {
		this.status = status;
	}
}

enum BeerCheckStatus {
	OK, NOT_OK
}