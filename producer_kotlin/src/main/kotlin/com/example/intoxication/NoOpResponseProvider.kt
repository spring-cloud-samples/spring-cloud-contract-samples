package com.example.intoxication

import org.springframework.stereotype.Service

/**
 * @author Marcin Grzejszczak
 */
@Service
internal class NoOpResponseProvider : ResponseProvider {
    override fun thereYouGo(personToCheck: Customer): Response {
        return Response(DrunkLevel.SOBER, DrunkLevel.SOBER)
    }
}
