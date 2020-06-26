package com.example;

import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

/**
 * @author Marcin Grzejszczak
 */
@Service
public class AgeCheckingPersonCheckingService implements PersonCheckingService {

	private final StreamBridge source;

	public AgeCheckingPersonCheckingService(StreamBridge source) {
		this.source = source;
	}

	@Override
	public Boolean shouldGetBeer(PersonToCheck personToCheck) {
		//remove::start[]
		boolean shouldGetBeer = personToCheck.age >= 20;
		this.source.send("output-out-0", new Verification(shouldGetBeer, "foo", "bar"));
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
