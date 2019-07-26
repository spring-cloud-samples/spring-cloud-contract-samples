package com.example;

//remove::start[]

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.testng.annotations.BeforeTest;

//remove::end[]

public abstract class BeerStuffBase {
	//remove::start[]

	@BeforeTest
	public void setup() {
		RestAssuredMockMvc.standaloneSetup(new StuffController());
	}
	//remove::end[]
}
