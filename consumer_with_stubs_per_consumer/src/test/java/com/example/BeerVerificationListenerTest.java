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
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.BDDAssertions.then;

/**
 * @author Marcin Grzejszczak
 */
@SpringBootTest(webEnvironment = WebEnvironment.MOCK,
		classes = {BeerVerificationListenerWithConsumerNameTest.Config.class, ClientApplication.class},
		properties = {"spring.application.name=foo-consumer"})
@AutoConfigureStubRunner(stubsMode = StubRunnerProperties.StubsMode.LOCAL,
		ids = "com.example:beer-api-producer-with-stubs-per-consumer",
		stubsPerConsumer = true)
@DirtiesContext
@DisabledIfEnvironmentVariable(named = "SKIP_COMPATIBILITY_TESTS", matches = "true")
public class BeerVerificationListenerTest extends AbstractTest {

	@Autowired StubTrigger stubTrigger;
	@Autowired BeerVerificationListener listener;

	@Test public void should_increase_the_eligible_counter_when_verification_was_accepted() throws Exception {
		int initialCounter = this.listener.eligibleCounter.get();


		this.stubTrigger.trigger("accepted_verification");


		then(this.listener.eligibleCounter.get()).isGreaterThan(initialCounter);
	}

	@Test public void should_increase_the_noteligible_counter_when_verification_was_rejected() throws Exception {
		int initialCounter = this.listener.notEligibleCounter.get();


		this.stubTrigger.trigger("rejected_verification");


		then(this.listener.notEligibleCounter.get()).isGreaterThan(initialCounter);
	}

	@TestConfiguration
	@ImportAutoConfiguration(TestChannelBinderConfiguration.class)
	static class Config {

	}
}
