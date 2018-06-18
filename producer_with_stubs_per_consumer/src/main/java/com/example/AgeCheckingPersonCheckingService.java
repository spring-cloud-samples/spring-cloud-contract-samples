package com.example;

import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

/**
 * @author Marcin Grzejszczak
 */
@Service
public class AgeCheckingPersonCheckingService implements PersonCheckingService {

	private final Source source;

	public AgeCheckingPersonCheckingService(Source source) {
		this.source = source;
	}

	@Override
	public Boolean shouldGetBeer(PersonToCheck personToCheck) {
		//remove::start[]
		boolean shouldGetBeer = personToCheck.age >= 20;
		source.output().send(MessageBuilder.withPayload(
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
			return eligible;
		}

		public void setEligible(boolean eligible) {
			this.eligible = eligible;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getSurname() {
			return surname;
		}

		public void setSurname(String surname) {
			this.surname = surname;
		}
	}
}
