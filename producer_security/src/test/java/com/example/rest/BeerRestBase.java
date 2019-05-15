package com.example.rest;

// remove::start[]

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.example.ProducerApplication;

import io.restassured.module.mockmvc.RestAssuredMockMvc;

// remove::end[]

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ProducerApplication.class)
@WebAppConfiguration
public abstract class BeerRestBase {

    // remove::start[]
    @Autowired
    private WebApplicationContext context;

    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    @Before
    public void setup() {
        RestAssuredMockMvc.standaloneSetup(MockMvcBuilders.webAppContextSetup(context).addFilter(springSecurityFilterChain));
    }
    // remove::end[]
}
