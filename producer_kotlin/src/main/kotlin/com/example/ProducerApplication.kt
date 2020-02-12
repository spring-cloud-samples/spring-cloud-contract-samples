package com.example

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.Message
import reactor.core.publisher.EmitterProcessor
import reactor.core.publisher.Flux
import java.util.function.Supplier

@SpringBootApplication
open class ProducerApplication

fun main(args: Array<String>) {
    SpringApplication.run(ProducerApplication::class.java, *args)
}

@Configuration
open class Config {

    @Bean
    open fun verificationEmitterProcessor(): EmitterProcessor<Message<AgeCheckingPersonCheckingService.Verification?>?>? {
        return EmitterProcessor.create()
    }

    @Bean
    open fun output(emitterProcessor: EmitterProcessor<Message<AgeCheckingPersonCheckingService.Verification?>?>?): Supplier<Flux<Message<AgeCheckingPersonCheckingService.Verification?>?>?>? {
        return Supplier { emitterProcessor }
    }
}