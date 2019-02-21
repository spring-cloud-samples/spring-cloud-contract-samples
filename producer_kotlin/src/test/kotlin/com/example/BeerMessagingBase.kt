package com.example

import org.junit.Before
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cloud.contract.verifier.messaging.MessageVerifier
import org.springframework.cloud.contract.verifier.messaging.boot.AutoConfigureMessageVerifier
import org.springframework.test.context.junit4.SpringRunner
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@RunWith(SpringRunner::class)
@SpringBootTest(classes = arrayOf(ProducerApplication::class), webEnvironment = SpringBootTest.WebEnvironment.NONE)
@AutoConfigureMessageVerifier
abstract class BeerMessagingBase {
    @Inject
    internal var messaging: MessageVerifier<*>? = null
    @Autowired
    internal var personCheckingService: PersonCheckingService? = null

    @Before
    fun setup() {
        // let's clear any remaining messages
        // output == destination or channel name
        this.messaging!!.receive("output", 100, TimeUnit.MILLISECONDS)
    }

    fun clientIsOldEnough() {
        this.personCheckingService!!.shouldGetBeer(PersonToCheck(25))
    }

    fun clientIsTooYoung() {
        this.personCheckingService!!.shouldGetBeer(PersonToCheck(5))
    }
}
