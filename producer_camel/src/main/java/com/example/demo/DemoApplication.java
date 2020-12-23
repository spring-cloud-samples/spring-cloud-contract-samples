package com.example.demo;

import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.RouteBuilder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}

@Configuration
class RouteConfiguration {

	@Bean
	RoutesBuilder myRouter() {
		return new RouteBuilder() {
			@Override
			public void configure() {
				from(start())
						.bean(MyProcessor.class)
						.to(finish());
			}
		};
	}

	// rabbitmq://hostname[:port]/exchangeName?[options]
	String start() { return "rabbitmq:localhost/person?queue=person"; }

	String finish() {
		return "rabbitmq:localhost/verifications?queue=verifications";
	}

}

class Person {
	public Integer age;

	public Person(Integer age) {
		this.age = age;
	}

	public Person() {
	}

	public Integer getAge() {
		return this.age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}
}

class Verification {
	public boolean eligible;

	public Verification(boolean eligible) {
		this.eligible = eligible;
	}

	public Verification() {
	}

	@Override
	public String toString() {
		return "Verification{" +
				"eligible=" + this.eligible +
				'}';
	}

	public boolean isEligible() {
		return this.eligible;
	}

	public void setEligible(boolean eligible) {
		this.eligible = eligible;
	}
}