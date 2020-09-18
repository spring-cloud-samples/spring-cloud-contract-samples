package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ProducerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProducerApplication.class, args);
	}

	@Bean
	PersonCheckingService noOpPersonCheckingService() {
		return personToCheck -> personToCheck.getAge() >= 18;
	}
}

/*

grpcurl --plaintext -d "{\"age\": 30}" localhost:8008 beer.BeerService/check

 */