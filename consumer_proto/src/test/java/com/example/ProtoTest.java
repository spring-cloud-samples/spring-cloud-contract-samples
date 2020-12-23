package com.example;

import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.cloud.contract.stubrunner.junit.StubRunnerExtension;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

/**
 * @author Marcin Grzejszczak
 */
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
// @org.junit.Ignore
public class ProtoTest {

	@Autowired
	RestTemplate restTemplate;

	int port;

	//remove::start[]
	@RegisterExtension
	static StubRunnerExtension rule = new StubRunnerExtension()
			.downloadStub("com.example", "beer-api-producer-proto")
			.stubsMode(StubRunnerProperties.StubsMode.LOCAL);

	// remove::end[]

	@BeforeAll
	public static void beforeClass() {
		Assumptions.assumeTrue(atLeast210(), "Spring Cloud Contract must be in version at least 2.1.0");
		Assumptions.assumeTrue(StringUtils.isEmpty(System.getenv("OLD_PRODUCER_TRAIN")),
				"Env var OLD_PRODUCER_TRAIN must not be set");
	}

	@BeforeEach
	public void setupPort() {
		//remove::start[]
		this.port = rule.findStubUrl("beer-api-producer-proto").getPort();
		// remove::end[]
	}

	private static boolean atLeast210() {
		try {
			Class.forName(
					"org.springframework.cloud.contract.verifier.util.ContractVerifierUtil");
		}
		catch (Exception ex) {
			return false;
		}
		return true;
	}

	// remove::end[]
	// tag::tests[]
	@Test
	public void should_give_me_a_beer_when_im_old_enough() throws Exception {
		//remove::start[]
		Beer.Response response = this.restTemplate.postForObject(
				"http://localhost:" + this.port + "/check",
				Beer.PersonToCheck.newBuilder().setAge(23).build(), Beer.Response.class);

		BDDAssertions.then(response.getStatus()).isEqualTo(Beer.Response.BeerCheckStatus.OK);
		// remove::end[]
	}

	@Test
	public void should_reject_a_beer_when_im_too_young() throws Exception {
		//remove::start[]
		Beer.Response response = this.restTemplate.postForObject(
				"http://localhost:" + this.port + "/check",
				Beer.PersonToCheck.newBuilder().setAge(17).build(), Beer.Response.class);
		// TODO: If someone knows how to do this properly for default responses that would be helpful
		response = response == null ? Beer.Response.newBuilder().build() : response;

		BDDAssertions.then(response.getStatus()).isEqualTo(Beer.Response.BeerCheckStatus.NOT_OK);
		// remove::end[]
	}
	// end::tests[]

}
