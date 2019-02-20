package com.example

import org.junit.Before
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
//remove::start[]
import org.springframework.cloud.contract.verifier.messaging.MessageVerifier
import org.springframework.cloud.contract.verifier.messaging.boot.AutoConfigureMessageVerifier
//remove::end[]
import org.springframework.test.context.junit4.SpringRunner
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@RunWith(SpringRunner::class)
@SpringBootTest(classes = arrayOf(ProducerApplication::class), webEnvironment = SpringBootTest.WebEnvironment.NONE)
//remove::start[]
@AutoConfigureMessageVerifier
abstract//remove::end[]
class BeerMessagingBase {
    //remove::start[]
    @Inject
    internal var messaging: MessageVerifier<*>? = null
    //remove::end[]
    @Autowired
    internal var personCheckingService: PersonCheckingService? = null

    @Before
    fun setup() {
        // let's clear any remaining messages
        // output == destination or channel name
        //remove::start[]
        this.messaging!!.receive("output", 100, TimeUnit.MILLISECONDS)
        //remove::end[]
    }

    fun clientIsOldEnough() {
        //remove::start[]
        this.personCheckingService!!.shouldGetBeer(PersonToCheck(25))
        //remove::end[]
    }

    fun clientIsTooYoung() {
        //remove::start[]
        this.personCheckingService!!.shouldGetBeer(PersonToCheck(5))
        //remove::end[]
    }
}
