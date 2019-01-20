package com.example;

//remove::start[]
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.Before;

//remove::end[]

public abstract class BeerStuffBase {
	//remove::start[]

	@Before
	public void setup() {
		RestAssuredMockMvc.standaloneSetup(new StuffController());
	}
	//remove::end[]
}
