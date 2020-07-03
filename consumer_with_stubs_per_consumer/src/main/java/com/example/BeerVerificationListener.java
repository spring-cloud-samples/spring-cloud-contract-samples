package com.example;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.stereotype.Component;

/**
 * @author Marcin Grzejszczak
 */
@Component("input")
class BeerVerificationListener implements Consumer<BeerVerificationListener.Verification> {

	private static final Log log = LogFactory.getLog(BeerVerificationListener.class);

	AtomicInteger eligibleCounter = new AtomicInteger();
	AtomicInteger notEligibleCounter = new AtomicInteger();

	@Override
	public void accept(Verification verification) {
		log.info("Received new verification");
		//remove::start[]
		//tag::listener[]
		if (verification.eligible) {
			this.eligibleCounter.incrementAndGet();
		} else {
			this.notEligibleCounter.incrementAndGet();
		}
		//end::listener[]
		//remove::end[]
	}

	public static class Verification {
		public boolean eligible;
		public String name;

		@Override public String toString() {
			return "Verification{" + "eligible=" + this.eligible + ", foo='" + this.name + '\''
					+ '}';
		}
	}
}
