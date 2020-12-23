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
		//tag::impl[]
		boolean shouldGetBeer = personToCheck.age >= 20;
		this.source.send("verifications", new Verification(shouldGetBeer));
		return shouldGetBeer;
		//end::impl[]
		//remove::end[return]
	}

	public static class Verification {
		boolean eligible;

		public Verification(boolean eligible) {
			this.eligible = eligible;
		}

		public Verification() {
		}

		public boolean isEligible() {
			return this.eligible;
		}

		public void setEligible(boolean eligible) {
			this.eligible = eligible;
		}
	}
}
