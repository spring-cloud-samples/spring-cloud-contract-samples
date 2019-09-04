package com.example.intoxication

import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

/**
 * Scenario based controller
 */
@RestController
class BeerServingController(private val responseProvider: ResponseProvider) {

    @RequestMapping(value = ["/beer"],
            method = arrayOf(RequestMethod.POST),
            consumes = arrayOf("application/json"),
            produces = arrayOf("application/json"))
    fun check(@RequestBody customer: Customer): Response {
        return this.responseProvider.thereYouGo(customer)
    }

}

interface ResponseProvider {
    fun thereYouGo(personToCheck: Customer): Response
}

class Customer {
    //remove::start[]
    var name: String = ""

    constructor(name: String) {
        this.name = name
    }

    constructor() {}
    //remove::end[]
}

class Response(var previousStatus: DrunkLevel, var currentStatus: DrunkLevel)

enum class DrunkLevel {
    SOBER, TIPSY, DRUNK, WASTED
}