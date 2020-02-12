package com.example;

import reactor.core.publisher.EmitterProcessor;

import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

/**
 * @author Marcin Grzejszczak
 */
@Service
public class AgeCheckingPersonCheckingService implements PersonCheckingService {

	private final EmitterProcessor<Message<Verification>> source;

	public AgeCheckingPersonCheckingService(EmitterProcessor<Message<Verification>> source) {
		this.source = source;
	}

	@Override
	public Boolean shouldGetBeer(PersonToCheck personToCheck) {
		//remove::start[]
		boolean shouldGetBeer = personToCheck.age >= 20;
		this.source.onNext(MessageBuilder.withPayload(
				new Verification(shouldGetBeer, "foo", "bar")
		).build());
		return shouldGetBeer;
		//remove::end[return]
	}

	public static class Verification {
		boolean eligible;
		String name, surname;

		public Verification(boolean eligible, String name, String surname) {
			this.eligible = eligible;
			this.name = name;
			this.surname = surname;
		}

		public Verification() {
		}

		public boolean isEligible() {
			return this.eligible;
		}

		public void setEligible(boolean eligible) {
			this.eligible = eligible;
		}

		public String getName() {
			return this.name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getSurname() {
			return this.surname;
		}

		public void setSurname(String surname) {
			this.surname = surname;
		}
	}
}
