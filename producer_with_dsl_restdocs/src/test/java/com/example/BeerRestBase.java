package com.example;

//remove::start[]
import io.restassured.module.mockmvc.RestAssuredMockMvc;
//remove::end[]

import java.util.Random;

import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
//remove::start[]
import org.springframework.restdocs.JUnitRestDocumentation;
//remove::end[]
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.argThat;
//remove::start[]
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
//remove::end[]

@RunWith(MockitoJUnitRunner.class)
public abstract class BeerRestBase {
	//remove::start[]
	private static final String OUTPUT = "target/generated-snippets";

	@Rule
	public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation(OUTPUT);

	@Rule public TestName testName = new TestName();

	@Mock PersonCheckingService personCheckingService;
	@Mock StatsService statsService;
	@InjectMocks ProducerController producerController;
	@InjectMocks StatsController statsController;

	@Before
	public void setup() {
		given(personCheckingService.shouldGetBeer(argThat(oldEnough()))).willReturn(true);
		given(statsService.findBottlesByName(anyString())).willReturn(new Random().nextInt());
		RestAssuredMockMvc.mockMvc(MockMvcBuilders.standaloneSetup(producerController, statsController)
				.apply(documentationConfiguration(this.restDocumentation))
				.alwaysDo(document(getClass().getSimpleName() + "_" + testName.getMethodName()))
				.build());
	}

	private ArgumentMatcher<PersonToCheck> oldEnough() {
		return argument -> argument.age >= 20;
	}
	//remove::end[]
}
