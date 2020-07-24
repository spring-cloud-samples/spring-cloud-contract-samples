package com.example

import io.restassured.config.EncoderConfig
import io.restassured.module.mockmvc.RestAssuredMockMvc
import io.restassured.module.mockmvc.config.RestAssuredMockMvcConfig
import org.junit.jupiter.api.BeforeEach
import java.util.*

abstract class BeerRestBase {
    internal var producerController = ProducerController(oldEnough())
    internal var statsController = StatsController(statsService())

    @BeforeEach
    fun setup() {
        val encoderConfig: EncoderConfig = EncoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false)
        val restAssuredConf: RestAssuredMockMvcConfig = RestAssuredMockMvcConfig().encoderConfig(encoderConfig)
        RestAssuredMockMvc.config = restAssuredConf
        RestAssuredMockMvc.standaloneSetup(this.producerController, this.statsController)
    }

    private fun oldEnough(): PersonCheckingService {
        return object : PersonCheckingService {
            override fun shouldGetBeer(personToCheck: PersonToCheck): Boolean? {
                return personToCheck.age >= 20
            }
        }
    }

    private fun statsService(): StatsService {
        return object : StatsService {
            override fun findBottlesByName(name: String): Int {
                return Random().nextInt()
            }
        }
    }
}
