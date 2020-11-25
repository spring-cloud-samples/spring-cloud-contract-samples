package com.example;

import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.junit.StubRunnerExtension;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.web.client.RestTemplate;

// remove::start[]
// remove::end[]

/**
 * @author Marcin Grzejszczak
 */
@SpringBootTest
//@org.junit.jupiter.api.Disabled
public class GenerateStubsTest {

	//remove::start[]
	@RegisterExtension
	static StubRunnerExtension rule = new StubRunnerExtension()
			.downloadStub("com.example","beer-api-producer-latest", "0.0.1-SNAPSHOT")
			.repoRoot("stubs://file://" + System.getenv("ROOT") + "/producer_with_latest_3_0_features_gradle/src/contractTest/resources/contracts/beer/in_progress")
			.stubsMode(StubRunnerProperties.StubsMode.REMOTE)
			.withGenerateStubs(true);
	//remove::end[]

	//tag::tests[]
	@Test public void should_generate_a_stub_at_runtime() throws Exception {
		//remove::start[]
		int port = rule.findStubUrl("beer-api-producer-latest").getPort();

		String object = new RestTemplate().getForObject("http://localhost:" + port + "/stuff", String.class);

		BDDAssertions.then(object).isEqualTo("OK");
		//remove::end[]
	}
	//end::tests[]
}
