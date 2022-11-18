package com.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfEnvironmentVariable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cloud.contract.stubrunner.StubTrigger;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;


import static org.assertj.core.api.BDDAssertions.then;

/**
 * @author Marcin Grzejszczak
 */
@SpringBootTest(classes = {ClientApplication.class, BeerVerificationListenerClasspathTest.Config.class},
		webEnvironment = WebEnvironment.NONE)
@AutoConfigureStubRunner(ids = "com.example:beer-api-producer-restdocs")
@DisabledIfEnvironmentVariable(named = "SKIP_COMPATIBILITY_TESTS", matches = "true")
public class BeerVerificationListenerClasspathTest extends AbstractTest {

	@Autowired StubTrigger stubTrigger;
	@Autowired BeerVerificationListener listener;

	@Test public void should_increase_the_eligible_counter_when_verification_was_accepted() throws Exception {
		int initialCounter = this.listener.eligibleCounter.get();

		// trigger the message
		
		this.stubTrigger.trigger("accepted_verification");
		

		then(this.listener.eligibleCounter.get()).isGreaterThan(initialCounter);
	}

	@Test public void should_increase_the_noteligible_counter_when_verification_was_rejected() throws Exception {
		int initialCounter = this.listener.notEligibleCounter.get();

		// trigger the message
		
		this.stubTrigger.trigger("rejected_verification");
		

		then(this.listener.notEligibleCounter.get()).isGreaterThan(initialCounter);
	}

	@TestConfiguration
	@ImportAutoConfiguration(TestChannelBinderConfiguration.class)
	static class Config {

	}
}
