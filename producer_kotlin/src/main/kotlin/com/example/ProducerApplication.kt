package com.example

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.stream.annotation.EnableBinding
import org.springframework.cloud.stream.messaging.Source

@SpringBootApplication
@EnableBinding(Source::class)
open class ProducerApplication

fun main(args: Array<String>) {
    SpringApplication.run(ProducerApplication::class.java, *args)
}
