package com.example

import io.restassured.module.mockmvc.RestAssuredMockMvc
import org.junit.Before

abstract class BeerStuffBase {
    @Before
    fun setup() {
        RestAssuredMockMvc.standaloneSetup(StuffController())
    }
}
