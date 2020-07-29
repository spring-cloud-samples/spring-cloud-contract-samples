package com.ideabaker.samples.scc.security.consumerwebflux.config

import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.web.reactive.WebFluxProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.web.reactive.config.EnableWebFlux
import org.springframework.web.reactive.config.WebFluxConfigurer
import org.springframework.web.reactive.function.client.ExchangeFilterFunction
import org.springframework.web.reactive.function.client.WebClient

/**
 *
 * @author Arthur Kazemi<bidadh@gmail.com>
 * @since 2019-01-13 18:47
 */
@Configuration
@EnableConfigurationProperties(WebFluxProperties::class)
class WebConfig : WebFluxConfigurer {
  private val logger = LoggerFactory.getLogger(WebConfig::class.java)

  @Bean
  fun webClientBuilder(): WebClient.Builder {
    return WebClient.builder()
        .filter(logRequest())
  }

  private fun logRequest(): ExchangeFilterFunction {
    return ExchangeFilterFunction { request, next ->
      if (logger.isDebugEnabled) {
        logger.debug("Request: {} {}", request.method(), request.url())
        request.headers()
            .filter { entry -> entry.key != HttpHeaders.AUTHORIZATION }
            .forEach { (name, values) -> logger.debug("{}={}", name, values.reduce { s1, s2 -> "$s1,$s2" }) }
      }
      next.exchange(request)
    }
  }
}