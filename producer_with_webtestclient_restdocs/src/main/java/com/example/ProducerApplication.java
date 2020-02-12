package com.example;

import java.util.function.Supplier;

import com.example.model.Verification;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;

@SpringBootApplication

public class ProducerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProducerApplication.class, args);
	}

	@Bean
	EmitterProcessor<Message<Verification>> verificationEmitterProcessor() {
		return EmitterProcessor.create();
	}

	@Bean
	Supplier<Flux<Message<Verification>>> output(EmitterProcessor<Message<Verification>> emitterProcessor) {
		return () -> emitterProcessor;
	}
}
