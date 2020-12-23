package com.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfEnvironmentVariable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.cloud.contract.stubrunner.StubTrigger;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.BDDAssertions.then;

/**
 * @author Marcin Grzejszczak
 */
@SpringBootTest(classes = ClientApplication.class, webEnvironment = WebEnvironment.NONE)
//remove::start[]
@AutoConfigureStubRunner(stubsMode = StubRunnerProperties.StubsMode.LOCAL, ids = "com.example:beer-api-producer-external")
//remove::end[]
@DirtiesContext
@DisabledIfEnvironmentVariable(named = "SKIP_COMPATIBILITY_TESTS", matches = "true")
//@org.junit.jupiter.api.Disabled
public class ExternalBeerVerificationListenerTest extends AbstractTest {

	//remove::start[]
	@Autowired StubTrigger stubTrigger;
	//remove::end[]
	@Autowired BeerVerificationListener listener;

	@Test public void should_increase_the_eligible_counter_when_positive_verification_takes_place() throws Exception {
		int initialCounter = this.listener.eligibleCounter.get();

		//remove::start[]
		this.stubTrigger.trigger("accepted_verification");
		//remove::end[]

		then(this.listener.eligibleCounter.get()).isGreaterThan(initialCounter);
	}

	@Test public void should_increase_the_noteligible_counter_when_negative_verification_takes_place() throws Exception {
		int initialCounter = this.listener.notEligibleCounter.get();

		//remove::start[]
		this.stubTrigger.trigger("rejected_verification");
		//remove::end[]

		then(this.listener.notEligibleCounter.get()).isGreaterThan(initialCounter);
	}
}
