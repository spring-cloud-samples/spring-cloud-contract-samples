package com.example

import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
class ProducerController(private val personCheckingService: PersonCheckingService) {

    //remove::start[]
    @RequestMapping(value = ["/check"],
            method = arrayOf(RequestMethod.POST),
            consumes = arrayOf("application/json"),
            produces = arrayOf("application/json"))
    fun check(@RequestBody personToCheck: PersonToCheck): Response {
        return if (this.personCheckingService.shouldGetBeer(personToCheck)!!) {
            Response(BeerCheckStatus.OK)
        } else Response(BeerCheckStatus.NOT_OK)
    }
    //remove::end[]

}

interface PersonCheckingService {
    fun shouldGetBeer(personToCheck: PersonToCheck): Boolean?
}

class PersonToCheck {
    var age: Int = 0

    constructor(age: Int) {
        this.age = age
    }

    constructor() {}
}

class Response(var status: BeerCheckStatus)

enum class BeerCheckStatus {
    OK, NOT_OK
}