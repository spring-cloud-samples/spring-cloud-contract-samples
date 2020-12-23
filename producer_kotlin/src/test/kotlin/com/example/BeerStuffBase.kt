package com.example

import io.restassured.config.EncoderConfig
import io.restassured.module.mockmvc.RestAssuredMockMvc
import io.restassured.module.mockmvc.config.RestAssuredMockMvcConfig
import org.junit.jupiter.api.BeforeEach

abstract class BeerStuffBase {
    @BeforeEach
    fun setup() {
		val encoderConfig: EncoderConfig = EncoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false)
		val restAssuredConf: RestAssuredMockMvcConfig = RestAssuredMockMvcConfig().encoderConfig(encoderConfig)
		RestAssuredMockMvc.config = restAssuredConf
		RestAssuredMockMvc.standaloneSetup(StuffController())
    }
}
