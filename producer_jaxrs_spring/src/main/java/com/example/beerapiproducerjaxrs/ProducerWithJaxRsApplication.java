package com.example.beerapiproducerjaxrs;

import org.glassfish.jersey.server.ResourceConfig;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class ProducerWithJaxRsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProducerWithJaxRsApplication.class, args);
	}

	@Bean
	PersonCheckingService personCheckingService() {
		return argument -> argument.getAge() >= 20;
	}
}

@Component
class JerseyConfig extends ResourceConfig {
	JerseyConfig() {
		register(FraudDetectionController.class);
	}
}