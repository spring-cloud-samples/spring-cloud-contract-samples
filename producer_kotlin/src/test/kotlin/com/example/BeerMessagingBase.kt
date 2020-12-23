package com.example

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cloud.contract.verifier.messaging.boot.AutoConfigureMessageVerifier
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration

@SpringBootTest(classes = arrayOf(ProducerApplication::class), webEnvironment = SpringBootTest.WebEnvironment.NONE)
@AutoConfigureMessageVerifier
@ImportAutoConfiguration(TestChannelBinderConfiguration::class)
abstract class BeerMessagingBase {
    @Autowired
    internal var personCheckingService: PersonCheckingService? = null

    fun clientIsOldEnough() {
        this.personCheckingService!!.shouldGetBeer(PersonToCheck(25))
    }

    fun clientIsTooYoung() {
        this.personCheckingService!!.shouldGetBeer(PersonToCheck(5))
    }
}
