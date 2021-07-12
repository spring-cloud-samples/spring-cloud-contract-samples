package com.example

import org.springframework.cloud.stream.function.StreamBridge
import org.springframework.stereotype.Service

/**
 * @author Marcin Grzejszczak
 */
@Service
class AgeCheckingPersonCheckingService(private val source: StreamBridge) : PersonCheckingService {

    override fun shouldGetBeer(personToCheck: PersonToCheck): Boolean? {
        
        
        val shouldGetBeer = personToCheck.age >= 20
        this.source.send("output-out-0", Verification(shouldGetBeer))
        return shouldGetBeer
        
        
    }

    class Verification {
        var isEligible: Boolean = false

        constructor(eligible: Boolean) {
            this.isEligible = eligible
        }

        constructor() {}
    }
}
