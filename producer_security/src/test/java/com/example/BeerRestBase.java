package com.example;

// remove::start[]

import static io.restassured.module.mockmvc.RestAssuredMockMvc.webAppContextSetup;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.WebApplicationContext;

// remove::end[]

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = ProducerController.class)
@ContextConfiguration(classes = ProducerApplication.class)
/*
 * @Import({ ResourceServerConfiguration.class, ResourceServerTokenServicesConfiguration.class, OAuth2AutoConfiguration.class,
 * ResourceServerTokenRelayAutoConfiguration.class })
 */
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
