package com.example;

import java.net.URI;

import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.cloud.contract.stubrunner.junit.StubRunnerExtension;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

/**
 * @author Marcin Grzejszczak
 */
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
public class BeerControllerGraphQLTest extends AbstractTest {

	//remove::start[]
	@RegisterExtension
	static StubRunnerExtension rule = new StubRunnerExtension()
			.downloadStub("com.example","beer-api-producer-graphql")
			.stubsMode(StubRunnerProperties.StubsMode.LOCAL);
	//remove::end[]

	@BeforeAll
	public static void beforeClass() {
		Assumptions.assumeTrue(atLeast300(), "Spring Cloud Contract must be in version at least 3.0.0");
		Assumptions.assumeTrue(StringUtils.isEmpty(System.getenv("OLD_PRODUCER_TRAIN")), "Env var OLD_PRODUCER_TRAIN must not be set");
	}

	private static boolean atLeast300() {
		try {
			Class.forName("org.springframework.cloud.contract.verifier.dsl.wiremock.SpringCloudContractRequestMatcher");
		} catch (Exception ex) {
			return false;
		}
		return true;
	}

	private static final String REQUEST_BODY = "{\n"
			+ "\"query\":\"query queryName($personName: String!) {\\n  personToCheck(name: $personName) {\\n    name\\n    age\\n  }\\n}\","
			+ "\"variables\":{\"personName\":\"Old Enough\"},\n"
			+ "\"operationName\":\"queryName\"\n"
			+ "}";


	//remove::start[]
	@Test
	public void should_send_a_graphql_request() throws Exception {
		ResponseEntity<String> responseEntity = new RestTemplate()
				.exchange(RequestEntity
						.post(URI.create("http://localhost:" + rule.findStubUrl("beer-api-producer-graphql").getPort() + "/graphql"))
						.contentType(MediaType.APPLICATION_JSON)
						.body(REQUEST_BODY), String.class);

		BDDAssertions.then(responseEntity.getStatusCodeValue()).isEqualTo(200);

	}
	//remove::end[]
}
