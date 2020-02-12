package com.example;

import reactor.core.publisher.EmitterProcessor;

import org.springframework.stereotype.Service;

/**
 * @author Marcin Grzejszczak
 */
@Service
public class AgeCheckingPersonCheckingService implements PersonCheckingService {

	private final EmitterProcessor<Verification> emitterProcessor;

	public AgeCheckingPersonCheckingService(EmitterProcessor<AgeCheckingPersonCheckingService.Verification> emitterProcessor) {
		this.emitterProcessor = emitterProcessor;
	}

	@Override
	public Boolean shouldGetBeer(PersonToCheck personToCheck) {
		//remove::start[]
		//tag::impl[]
		boolean shouldGetBeer = personToCheck.age >= 20;
		this.emitterProcessor.onNext(new Verification(shouldGetBeer));
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
