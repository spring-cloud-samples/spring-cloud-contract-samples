package com.example.rest;

// remove::start[]

import static io.restassured.module.mockmvc.RestAssuredMockMvc.webAppContextSetup;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.WebApplicationContext;

import com.example.ProducerApplication;

// remove::end[]

@WebMvcTestImports
@RunWith(SpringRunner.class)
@WebMvcTest(controllers = ProducerController.class)
// @SpringBootTest(classes = ProducerApplication.class)
@ContextConfiguration(classes = ProducerApplication.class)
public abstract class BeerRestBase {

    // remove::start[]
    @Autowired
    private WebApplicationContext context;

    @Before
    public void setup() {
        webAppContextSetup(context);
    }
    // remove::end[]
}
