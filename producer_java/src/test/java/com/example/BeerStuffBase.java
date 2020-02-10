package com.example;

//remove::start[]
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;

//remove::end[]

public abstract class BeerStuffBase {
	//remove::start[]

	@BeforeEach
	public void setup() {
		RestAssuredMockMvc.standaloneSetup(new StuffController());
	}
	//remove::end[]
}
