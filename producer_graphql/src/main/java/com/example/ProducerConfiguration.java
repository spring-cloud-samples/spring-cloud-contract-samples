package com.example;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProducerConfiguration {

	@Bean
	PeopleToCheckDataFetcher peopleToCheckDataFetcher(PersonToCheckService personToCheckService) {
		return new PeopleToCheckDataFetcher(personToCheckService);
	}

	@Bean
	PersonToCheckDataFetcher personToCheckDataFetcher(PersonToCheckService personToCheckService) {
		return new PersonToCheckDataFetcher(personToCheckService);
	}

	@Bean
	PersonToCheckService personToCheckService() {
		return new PersonToCheckService();
	}
}
