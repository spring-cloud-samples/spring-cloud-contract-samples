package com.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfEnvironmentVariable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
//remove::start[]
import org.springframework.cloud.contract.stubrunner.StubTrigger;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
//remove::end[]

import static org.assertj.core.api.BDDAssertions.then;

/**
 * @author Marcin Grzejszczak
 */
@SpringBootTest(classes = ClientApplication.class, webEnvironment = WebEnvironment.NONE)
//remove::start[]
@AutoConfigureStubRunner(ids = "com.example:beer-api-producer-restdocs")
//remove::end[]
@DisabledIfEnvironmentVariable(named = "SKIP_COMPATIBILITY_TESTS", matches = "true")
public class BeerVerificationListenerClasspathTest extends AbstractTest {

	//remove::start[]
	@Autowired StubTrigger stubTrigger;
	//remove::end[]
	@Autowired BeerVerificationListener listener;

	@Test public void should_increase_the_eligible_counter_when_verification_was_accepted() throws Exception {
		int initialCounter = this.listener.eligibleCounter.get();

		// trigger the message
		//remove::start[]
		this.stubTrigger.trigger("accepted_verification");
		//remove::end[]

		then(this.listener.eligibleCounter.get()).isGreaterThan(initialCounter);
	}

	@Test public void should_increase_the_noteligible_counter_when_verification_was_rejected() throws Exception {
		int initialCounter = this.listener.notEligibleCounter.get();

		// trigger the message
		//remove::start[]
		this.stubTrigger.trigger("rejected_verification");
		//remove::end[]

		then(this.listener.notEligibleCounter.get()).isGreaterThan(initialCounter);
	}
}
