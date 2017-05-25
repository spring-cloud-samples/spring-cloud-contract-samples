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
		return null;
	}

	public static class Verification {
		boolean eligible;

		public Verification(boolean eligible) {
			this.eligible = eligible;
		}

		public Verification() {
		}

		public boolean isEligible() {
			return eligible;
		}

		public void setEligible(boolean eligible) {
			this.eligible = eligible;
		}
	}
}
