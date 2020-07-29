package com.example;

import java.util.Random;

//remove::start[]
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
//remove::end[]

//remove::start[]
@ExtendWith(RestDocumentationExtension.class)
//remove::end[]
public abstract class BeerRestBase {
	//remove::start[]

	ProducerController producerController = new ProducerController(oldEnough());
	StatsController statsController = new StatsController(statsService());

	private PersonCheckingService oldEnough() {
		return argument -> argument.age >= 20;
	}

	private StatsService statsService() {
		return name -> new Random().nextInt();
	}

	@BeforeEach
	public void setup(RestDocumentationContextProvider provider, TestInfo testInfo) {
		RestAssuredMockMvc.mockMvc(MockMvcBuilders.standaloneSetup(this.producerController, this.statsController)
				.apply(documentationConfiguration(provider))
				.alwaysDo(document(getClass().getSimpleName() + "_" + testInfo.getDisplayName()))
				.build());
	}
	//remove::end[]
}
