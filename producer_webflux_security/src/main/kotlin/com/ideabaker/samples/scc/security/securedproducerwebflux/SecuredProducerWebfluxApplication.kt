package com.ideabaker.samples.scc.security.securedproducerwebflux

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SecuredProducerWebfluxApplication

fun main(args: Array<String>) {
	runApplication<SecuredProducerWebfluxApplication>(*args)
}
