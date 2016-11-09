package com.example;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.cloud.contract.stubrunner.StubTrigger;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.BDDAssertions.then;

/**
 * @author Marcin Grzejszczak
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@AutoConfigureStubRunner(workOffline = true, ids = "com.example:beer-api-producer")
public class BeerVerificationListenerTest extends AbstractTest {

	@Autowired StubTrigger stubTrigger;
	@Autowired BeerVerificationListener listener;

	@Test public void should_increase_the_eligible_counter_when_im_old_enough() throws Exception {
		int initialCounter = listener.eligibleCounter.get();

		stubTrigger.trigger("accepted_verification");

		then(listener.eligibleCounter.get()).isGreaterThan(initialCounter);
	}

	@Test public void should_increase_the_noteligible_counter_when_im_old_enough() throws Exception {
		int initialCounter = listener.notEligibleCounter.get();

		stubTrigger.trigger("rejected_verification");

		then(listener.notEligibleCounter.get()).isGreaterThan(initialCounter);
	}
}


/*

	@Autowired StubTrigger stubTrigger;
	@Autowired BeerVerificationListener listener;

	@Test public void should_increase_the_eligible_counter_when_im_old_enough() throws Exception {
		int initialCounter = listener.eligibleCounter.get();

		stubTrigger.trigger("accepted_verification");

		then(listener.eligibleCounter.get()).isGreaterThan(initialCounter);
	}

	@Test public void should_increase_the_noteligible_counter_when_im_old_enough() throws Exception {
		int initialCounter = listener.notEligibleCounter.get();

		stubTrigger.trigger("rejected_verification");

		then(listener.notEligibleCounter.get()).isGreaterThan(initialCounter);
	}

 */