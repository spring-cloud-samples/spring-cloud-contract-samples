package com.example

import org.springframework.messaging.Message
import org.springframework.messaging.support.MessageBuilder
import org.springframework.stereotype.Service
import reactor.core.publisher.EmitterProcessor

/**
 * @author Marcin Grzejszczak
 */
@Service
class AgeCheckingPersonCheckingService(private val source: EmitterProcessor<Message<Verification?>?>) : PersonCheckingService {

    override fun shouldGetBeer(personToCheck: PersonToCheck): Boolean? {
        //remove::start[]
        //tag::impl[]
        val shouldGetBeer = personToCheck.age >= 20
        this.source.onNext(MessageBuilder.withPayload(Verification(shouldGetBeer)).build())
        return shouldGetBeer
        //end::impl[]
        //remove::end[return]
    }

    class Verification {
        var isEligible: Boolean = false

        constructor(eligible: Boolean) {
            this.isEligible = eligible
        }

        constructor() {}
    }
}
