package com.ideabaker.samples.scc.security.securedproducerwebflux.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain
import java.security.interfaces.RSAPublicKey

/**
 *
 * @author Arthur Kazemi<bidadh@gmail.com>
 * @since 2019-01-01 15:36
 */
@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
class SpringSecurityWebFluxConfig {

  @Autowired
  lateinit var jwtAuthoritiesAwareAuthConverter: JwtAuthoritiesAuthenticationConverter

  @Autowired
  lateinit var publicKey: RSAPublicKey

  @Bean
  @Throws(Exception::class)
  internal fun springWebFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
    http
        .csrf().disable()
        .authorizeExchange()
        .pathMatchers(HttpMethod.OPTIONS, "/**").permitAll()
        .pathMatchers(HttpMethod.GET, "/contact/search**").authenticated()
        .anyExchange().authenticated()
        .and()
        .oauth2ResourceServer()
        .jwt()
        .jwtAuthenticationConverter(jwtAuthoritiesAwareAuthConverter)
        .publicKey(publicKey)

    return http.build()
  }
}