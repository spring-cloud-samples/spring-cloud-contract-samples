package com.example;

//remove::start[]
import java.util.Random;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.Rule;
import org.junit.rules.TestName;

import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;

//remove::end[]

public abstract class BeerRestBase {
	//remove::start[]
	private static final String OUTPUT = "target/generated-snippets";

	@Rule
	public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation(OUTPUT);

	@Rule public TestName testName = new TestName();

	ProducerController producerController = new ProducerController(oldEnough());
	StatsController statsController = new StatsController(statsService());

	private PersonCheckingService oldEnough() {
		return argument -> argument.age >= 20;
	}

	private StatsService statsService() {
		return name -> new Random().nextInt();
	}

	@BeforeEach
	public void setup() {
		RestAssuredMockMvc.mockMvc(MockMvcBuilders.standaloneSetup(this.producerController, this.statsController)
				.apply(documentationConfiguration(this.restDocumentation))
				.alwaysDo(document(getClass().getSimpleName() + "_" + this.testName.getMethodName()))
				.build());
	}
	//remove::end[]
}
