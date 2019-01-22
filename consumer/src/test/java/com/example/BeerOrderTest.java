package com.example;

/**
 * @author Olga Maciaszek-Sharma
 */

import java.math.BigDecimal;
import java.util.Arrays;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.junit.Assume;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.cloud.contract.stubrunner.junit.StubRunnerRule;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.StringUtils;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * @author Olga Maciaszek-Sharma
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@DirtiesContext
public class BeerOrderTest extends AbstractTest {

	//remove::start[]
	@Rule
	public StubRunnerRule rule = new StubRunnerRule()
			.downloadStub("com.example", "beer-api-producer-xml")
			.stubsMode(StubRunnerProperties.StubsMode.LOCAL);
	//remove::end[]

	@BeforeClass
	public static void beforeClass() {
		Assume.assumeTrue("Spring Cloud Contract must be in version at least 2.1.0", atLeast210());
		Assume.assumeTrue("Env var OLD_PRODUCER_TRAIN must not be set", StringUtils
				.isEmpty(System.getenv("OLD_PRODUCER_TRAIN")));
	}

	@Autowired
	MockMvc mockMvc;
	@Autowired
	BeerController beerController;

	//remove::start[]
	@Before
	public void setupPort() {
		beerController.port = this.rule.findStubUrl("beer-api-producer-xml").getPort();
	}
	//remove::end[]

	@Test
	public void shouldProcessBeerOrder() throws Exception {
		//remove::start[]
		XmlMapper xmlMapper = new XmlMapper();
		mockMvc.perform(MockMvcRequestBuilders.post("/order")
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
		mockMvc.perform(MockMvcRequestBuilders.post("/cancelOrder")
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