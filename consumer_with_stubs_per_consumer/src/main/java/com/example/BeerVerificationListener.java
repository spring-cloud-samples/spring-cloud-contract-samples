package com.example;

import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @author Marcin Grzejszczak
 */
@Component
class BeerVerificationListener {

	private static final Log log = LogFactory.getLog(BeerVerificationListener.class);

	AtomicInteger eligibleCounter = new AtomicInteger();
	AtomicInteger notEligibleCounter = new AtomicInteger();

	@StreamListener(Sink.INPUT)
	public void listen(Verification verification) {
		//remove::start[]
		log.info("Received new verification [" + verification + "]");
		if (verification.eligible && StringUtils.hasText(verification.name)) {
			eligibleCounter.incrementAndGet();
		} else {
			notEligibleCounter.incrementAndGet();
		}
		//remove::end[]
	}

	public static class Verification {
		public boolean eligible;
		public String name;

		@Override public String toString() {
			return "Verification{" + "eligible=" + eligible + ", foo='" + name + '\''
					+ '}';
		}
	}
}
