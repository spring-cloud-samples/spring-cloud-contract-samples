package com.example.intoxication

import com.example.intoxication.DrunkLevel.*
//remove::start[]
import io.restassured.module.mockmvc.RestAssuredMockMvc
//remove::end[]
import org.junit.Before
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.web.context.WebApplicationContext

/**
 * Tests for the scenario based stub
 */
@RunWith(SpringRunner::class)
@SpringBootTest(classes = arrayOf(BeerIntoxicationBase.Config::class))
abstract class BeerIntoxicationBase {

    @Autowired
    internal var webApplicationContext: WebApplicationContext? = null

    @Before
    fun setup() {
        //remove::start[]
        RestAssuredMockMvc.webAppContextSetup(this.webApplicationContext)
        //remove::end[]
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

    //tag::mock[]
    internal class MockResponseProvider : ResponseProvider {

        private var previous = SOBER
        private var current = SOBER

        override fun thereYouGo(personToCheck: Customer): Response {
            //remove::start[]
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
            //remove::end[]
            return Response(this.previous, this.current)
        }
    }
    //end::mock[]
}
