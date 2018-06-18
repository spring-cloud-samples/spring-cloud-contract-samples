package com.example;

import com.example.model.PersonToCheck;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * @author Marcin Grzejszczak
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ProducerControllerTests.Config.class)
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@DirtiesContext
public class ProducerControllerTests {

	@Autowired private MockMvc mockMvc;

	private JacksonTester<PersonToCheck> json;

	@Before
	public void setup() {
		ObjectMapper objectMappper = new ObjectMapper();
		// Possibly configure the mapper
		JacksonTester.initFields(this, objectMappper);
	}

	@Test
	public void should_grant_a_beer_when_person_is_old_enough() throws Exception {
		PersonToCheck personToCheck = new PersonToCheck(34);
	}

	@Test
	public void should_reject_a_beer_when_person_is_too_young() throws Exception {
		PersonToCheck personToCheck = new PersonToCheck(10);
	}

	@Configuration
	@EnableAutoConfiguration
	static class Config {
		//remove:start[]
		@Bean
		PersonCheckingService personCheckingService() {
			return personToCheck -> personToCheck.age >= 20;
		}

		@Bean
		ProducerController producerController(PersonCheckingService service) {
			return new ProducerController(service);
		}
	}
}
