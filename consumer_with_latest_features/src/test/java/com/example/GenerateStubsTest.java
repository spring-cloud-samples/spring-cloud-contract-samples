package com.example;

import org.assertj.core.api.BDDAssertions;
import org.junit.Assume;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.junit.StubRunnerRule;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

/**
 * @author Marcin Grzejszczak
 */
@RunWith(SpringRunner.class)
@SpringBootTest
//@org.junit.Ignore
public class GenerateStubsTest {

	//remove::start[]
	@Rule
	public StubRunnerRule rule = new StubRunnerRule()
			.downloadStub("com.example","beer-api-producer-latest", "0.0.1.BUILD-SNAPSHOT")
			.repoRoot("stubs://file://" + System.getenv("ROOT") + "/producer_with_latest_features/src/test/resources/contracts/beer/in_progress")
			.stubsMode(StubRunnerProperties.StubsMode.REMOTE)
			.withGenerateStubs(true);
	//remove::end[]

	//tag::tests[]
	@Test public void should_generate_a_stub_at_runtime() throws Exception {
		//remove::start[]
		int port = this.rule.findStubUrl("beer-api-producer-latest").getPort();

		String object = new RestTemplate().getForObject("http://localhost:" + port + "/stuff", String.class);

		BDDAssertions.then(object).isEqualTo("OK");
		//remove::end[]
	}
	//end::tests[]
}
