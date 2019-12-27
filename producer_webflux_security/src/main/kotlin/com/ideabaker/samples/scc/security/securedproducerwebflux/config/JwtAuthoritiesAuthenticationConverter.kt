package com.ideabaker.samples.scc.security.securedproducerwebflux.config

import org.springframework.core.convert.converter.Converter
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.util.StringUtils
import reactor.core.publisher.Mono
import java.util.stream.Collectors
import java.util.stream.Stream

/**
 *
 * @author Arthur Kazemi<bidadh@gmail.com>
 * @since 2019-01-20 14:32
 */
class JwtAuthoritiesAuthenticationConverter(private val delegate: JwtAuthenticationConverter) : Converter<Jwt, Mono<AbstractAuthenticationToken>> {

  private fun extractAuthorities(jwt: Jwt): Collection<GrantedAuthority> {
    val scopeAuthorities = delegate
        .convert(jwt)!!
        .authorities
        .stream()

    val authorities = attrs(jwt)
        .stream()
        .map { SimpleGrantedAuthority(it) }
    return Stream.concat(scopeAuthorities, authorities)
        .collect(Collectors.toList())
  }

  private fun attrs(jwt: Jwt): Collection<String> {
    val scopes = jwt.claims["authorities"]
    if (scopes is String) {
      return if (StringUtils.hasText(scopes)) {
        listOf(*scopes.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray())
      } else {
        emptyList()
      }
    } else if (scopes is Collection<*>) {
      @Suppress("UNCHECKED_CAST")
      return scopes as Collection<String>
    }

    return emptyList()
  }

  override fun convert(jwt: Jwt): Mono<AbstractAuthenticationToken>? {
    val authorities = this.extractAuthorities(jwt)
    val auth = JwtAuthenticationToken(jwt, authorities)
    return Mono.just(auth)
  }
}
