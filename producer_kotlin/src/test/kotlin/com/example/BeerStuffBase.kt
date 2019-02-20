package com.example

//remove::start[]
import io.restassured.module.mockmvc.RestAssuredMockMvc
//remove::end[]
import org.junit.Before

abstract class BeerStuffBase {
    //remove::start[]
    @Before
    fun setup() {
        RestAssuredMockMvc.standaloneSetup(StuffController())
    }
    //remove::end[]
}
