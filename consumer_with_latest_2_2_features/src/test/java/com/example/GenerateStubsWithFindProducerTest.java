package com.example;

import java.util.HashMap;
import java.util.Map;

import org.assertj.core.api.BDDAssertions;
import org.junit.Assume;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.junit.StubRunnerExtension;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

// remove::start[]
// remove::end[]

/**
 * @author Marcin Grzejszczak
 */
@SpringBootTest
// @org.junit.Ignore
public class GenerateStubsWithFindProducerTest {

	// remove::start[]
	static Map<String, String> contractProperties() {
		Map<String, String> map = new HashMap<>();
		map.put("stubs.find-producer", "true");
		return map;
	}

	@RegisterExtension
	public StubRunnerExtension rule = new StubRunnerExtension()
			.downloadStub("com.example:some-artifact-id:0.0.1")
			.downloadStub("com.example:some-other-artifact-id")
			.repoRoot("stubs://file://" + System.getenv("ROOT")
					+ "/producer_with_latest_2_2_features/src/test/resources/contracts")
			.withProperties(contractProperties())
			.stubsMode(StubRunnerProperties.StubsMode.REMOTE).withGenerateStubs(true);

	// remove::end[]

	@BeforeAll
	public static void beforeClass() {
		Assume.assumeTrue("Spring Cloud Contract must be in version at least 2.2.0",
				atLeast220());
		Assume.assumeTrue("Env var OLD_PRODUCER_TRAIN must not be set",
				StringUtils.isEmpty(System.getenv("OLD_PRODUCER_TRAIN")));
	}

	private static boolean atLeast220() {
		try {
			Class.forName(
					"org.springframework.cloud.contract.spec.internal.DslPropertyConverter");
		}
		catch (Exception ex) {
			return false;
		}
		return true;
	}

	// tag::tests[]
	@Test
	public void should_generate_a_stub_at_runtime_for_some_artifact_id()
			throws Exception {
		// remove::start[]
		int port = this.rule.findStubUrl("some-artifact-id").getPort();

		String object = new RestTemplate()
				.getForObject("http://localhost:" + port + "/stuff", String.class);

		BDDAssertions.then(object).isEqualTo("artifactId");
		// remove::end[]
	}

	@Test
	public void should_generate_a_stub_at_runtime_for_some_other_artifact_id()
			throws Exception {
		// remove::start[]
		int port = this.rule.findStubUrl("some-other-artifact-id").getPort();

		String object = new RestTemplate()
				.getForObject("http://localhost:" + port + "/stuff2", String.class);

		BDDAssertions.then(object).isEqualTo("artifactId2");
		// remove::end[]
	}
	// end::tests[]

}
