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
//@AutoConfigureStubRunner(stubsMode = StubRunnerProperties.StubsMode.LOCAL, ids = "com.example:beer-api-producer:+:stubs:8090")
@AutoConfigureStubRunner(stubsMode = StubRunnerProperties.StubsMode.REMOTE,
		repositoryRoot = "git://${ROOT}/target/contract_git/",
		ids = { "com.example:beer-api-producer-git:0.0.1-SNAPSHOT"})

@DirtiesContext
@DisabledIfEnvironmentVariable(named = "SKIP_COMPATIBILITY_TESTS", matches = "true")
//@org.junit.jupiter.api.Disabled
public class BeerVerificationListenerWithGitTest extends AbstractTest {

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
	
}
