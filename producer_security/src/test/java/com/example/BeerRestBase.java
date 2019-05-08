package com.example;

// remove::start[]

import org.junit.Before;

import io.restassured.module.mockmvc.RestAssuredMockMvc;

// remove::end[]

public abstract class BeerRestBase {

    // remove::start[]
    ProducerController producerController = new ProducerController(new PersonCheckingService());

    @Before
    public void setup() {
        RestAssuredMockMvc.standaloneSetup(this.producerController);
    }

    // remove::end[]
}
