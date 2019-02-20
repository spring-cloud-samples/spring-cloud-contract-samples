package com.example

import org.springframework.http.MediaType
import org.springframework.util.Assert
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class StuffController {

    @PostMapping(value = ["/stuff"],
            consumes = arrayOf(MediaType.APPLICATION_JSON_UTF8_VALUE),
            produces = arrayOf(MediaType.APPLICATION_JSON_UTF8_VALUE))
    internal fun stuff(@RequestBody stuff: Stuff): Stuff {
        Assert.isTrue(stuff.comment == LOREM_IPSUM, "Should have a matching comment")
        return Stuff(LOREM_IPSUM, "It worked", "success")
    }

    companion object {

        private val LOREM_IPSUM = "Lorem Ipsum is simply dummy text of the printing and typesetting industry.\nLorem Ipsum has been the industry's standard dummy text ever since the 1500s...\n"
    }
}

internal class Stuff {
    var comment: String = ""
    var message: String = ""
    var status: String = ""

    constructor(comment: String, message: String, status: String) {
        this.comment = comment
        this.message = message
        this.status = status
    }

    constructor() {}
}