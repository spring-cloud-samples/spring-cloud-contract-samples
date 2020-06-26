package com.example

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
open class ProducerApplication

fun main(args: Array<String>) {
    SpringApplication.run(ProducerApplication::class.java, *args)
}