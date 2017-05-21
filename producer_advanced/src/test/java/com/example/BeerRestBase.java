package com.example;

import com.jayway.restassured.module.mockmvc.RestAssuredMockMvc;

import org.junit.Before;

public abstract class BeerRestBase {

	@Before
	public void setup() {
		//remove::start[]
		RestAssuredMockMvc.standaloneSetup(new StoutController());
		//remove::end[]
	}

}