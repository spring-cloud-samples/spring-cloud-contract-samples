package com.example.intoxication

import com.example.intoxication.DrunkLevel.*
import io.restassured.module.mockmvc.RestAssuredMockMvc
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.context.WebApplicationContext

/**
 * Tests for the scenario based stub
 */
 // remove::start[]
@SpringBootTest(classes = arrayOf(BeerIntoxicationBase.Config::class))
// remove::end[]
abstract class BeerIntoxicationBase {

    // remove::start[]
    @Autowired
    internal var webApplicationContext: WebApplicationContext? = null

    @BeforeEach
    fun setup() {
        RestAssuredMockMvc.webAppContextSetup(this.webApplicationContext)
    }

    @Configuration
    @EnableAutoConfiguration
    open class Config {

        @Bean
        open fun controller(): BeerServingController {
            return BeerServingController(responseProvider())
        }

        @Bean
        open fun responseProvider(): ResponseProvider {
            return MockResponseProvider()
        }
    }

    internal class MockResponseProvider : ResponseProvider {

        private var previous = SOBER
        private var current = SOBER

        override fun thereYouGo(personToCheck: Customer): Response {
            if ("marcin" == personToCheck.name) {
                when (this.current) {
                    SOBER -> {
                        this.current = TIPSY
                        this.previous = SOBER
                    }
                    TIPSY -> {
                        this.current = DRUNK
                        this.previous = TIPSY
                    }
                    DRUNK -> {
                        this.current = WASTED
                        this.previous = DRUNK
                    }
                    WASTED -> throw UnsupportedOperationException("You can't handle it")
                }
            }
            return Response(this.previous, this.current)
        }
    }
    // remove::end[]
}
