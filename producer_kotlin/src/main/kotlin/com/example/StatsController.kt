package com.example

import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

/**
 * @author Marcin Grzejszczak
 */
@RestController
class StatsController(private val statsService: StatsService) {

    @RequestMapping(value = ["/stats"],
            method = arrayOf(RequestMethod.POST),
            consumes = arrayOf("application/json"),
            produces = arrayOf("application/json"))
    fun check(@RequestBody request: StatsRequest): StatsResponse {
        val bottles = this.statsService.findBottlesByName(request.name)
        val text = "Dear " + request.name + " thanks for your interested in drinking beer"
        return StatsResponse(bottles, text)
    }
}

interface StatsService {
    fun findBottlesByName(name: String): Int
}

@Service
internal class NoOpStatsService : StatsService {

    override fun findBottlesByName(name: String): Int {
        return 0
    }
}

class StatsRequest {
    var name: String = ""

    constructor(name: String) {
        this.name = name
    }

    constructor() {}
}

class StatsResponse {
    var quantity: Int = 0
    var text: String = ""

    constructor(quantity: Int, text: String) {
        this.quantity = quantity
        this.text = text
    }

    constructor() {}
}