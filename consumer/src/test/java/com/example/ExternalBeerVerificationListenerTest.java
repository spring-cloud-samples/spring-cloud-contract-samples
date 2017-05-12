package com.example;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.cloud.contract.stubrunner.StubTrigger;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.BDDAssertions.then;

/**
 * @author Marcin Grzejszczak
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
//remove::start[]
@AutoConfigureStubRunner(workOffline = true, ids = "com.example:beer-api-producer-external")
//remove::end[]
@DirtiesContext
//@org.junit.Ignore
public class ExternalBeerVerificationListenerTest extends AbstractTest {

	//remove::start[]
	@Autowired StubTrigger stubTrigger;
	//remove::end[]
	@Autowired BeerVerificationListener listener;

	@Test public void should_increase_the_eligible_counter_when_positive_verification_takes_place() throws Exception {
		int initialCounter = listener.eligibleCounter.get();

		//remove::start[]
		stubTrigger.trigger("accepted_verification");
		//remove::end[]

		then(listener.eligibleCounter.get()).isGreaterThan(initialCounter);
	}

	@Test public void should_increase_the_noteligible_counter_when_negative_verification_takes_place() throws Exception {
		int initialCounter = listener.notEligibleCounter.get();

		//remove::start[]
		stubTrigger.trigger("rejected_verification");
		//remove::end[]

		then(listener.notEligibleCounter.get()).isGreaterThan(initialCounter);
	}
}
