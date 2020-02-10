package com.example

import io.restassured.module.mockmvc.RestAssuredMockMvc
import org.junit.jupiter.api.BeforeEach

abstract class BeerStuffBase {
    @BeforeEach
    fun setup() {
        RestAssuredMockMvc.standaloneSetup(StuffController())
    }
}
