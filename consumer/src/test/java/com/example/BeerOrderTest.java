package com.example;

/**
 * @author Olga Maciaszek-Sharma
 */

import java.math.BigDecimal;
import java.util.Arrays;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.cloud.contract.stubrunner.junit.StubRunnerExtension;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.StringUtils;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * @author Olga Maciaszek-Sharma
 */
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@DirtiesContext
public class BeerOrderTest extends AbstractTest {

	//remove::start[]
	@RegisterExtension
	static StubRunnerExtension rule = new StubRunnerExtension()
			.downloadStub("com.example", "beer-api-producer-xml")
			.stubsMode(StubRunnerProperties.StubsMode.LOCAL);
	//remove::end[]

	@BeforeAll
	public static void beforeClass() {
		Assumptions.assumeTrue(atLeast210(), "Spring Cloud Contract must be in version at least 2.1.0");
		Assumptions.assumeTrue(StringUtils.isEmpty(System.getenv("OLD_PRODUCER_TRAIN")),
				"Env var OLD_PRODUCER_TRAIN must not be set");
	}

	@Autowired
	MockMvc mockMvc;
	@Autowired
	BeerController beerController;

	//remove::start[]
	@BeforeEach
	public void setupPort() {
		this.beerController.port = rule.findStubUrl("beer-api-producer-xml").getPort();
	}
	//remove::end[]

	@Test
	public void shouldProcessBeerOrder() throws Exception {
		//remove::start[]
		XmlMapper xmlMapper = new XmlMapper();
		this.mockMvc.perform(MockMvcRequestBuilders.post("/order")
				.contentType(MediaType.APPLICATION_XML)
				.content(xmlMapper
						.writeValueAsString(new BeerOrder(new BigDecimal("123"), Arrays
								.asList("abc", "def", "ghi")))))
				.andExpect(status().isOk());
		//remove::end[]
	}

	@Test
	public void shouldCancelBeerOrder() throws Exception {
		//remove::start[]
		XmlMapper xmlMapper = new XmlMapper();
		this.mockMvc.perform(MockMvcRequestBuilders.post("/cancelOrder")
				.contentType(MediaType.APPLICATION_XML)
				.content(xmlMapper
						.writeValueAsString(new BeerOrder(new BigDecimal("123"), Arrays
								.asList("abc", "def", "ghi")))))
				.andExpect(status().isOk());
		//remove::end[]
	}


	private static boolean atLeast210() {
		try {
			Class.forName("org.springframework.cloud.contract.verifier.util.ContractVerifierUtil");
		}
		catch (Exception ex) {
			return false;
		}
		return true;
	}
}