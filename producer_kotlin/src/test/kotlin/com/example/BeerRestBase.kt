package com.example


//remove::start[]
import io.restassured.module.mockmvc.RestAssuredMockMvc
//remove::end[]
import org.junit.Before
import java.util.*

abstract class BeerRestBase {
    //remove::start[]
    internal var producerController = ProducerController(oldEnough())
    internal var statsController = StatsController(statsService())

    @Before
    fun setup() {
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
    //remove::end[]
}
