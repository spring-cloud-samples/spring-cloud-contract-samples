package com.ideabaker.samples.scc.security.securedproducerwebflux

import com.ideabaker.samples.scc.security.securedproducerwebflux.model.UserInfo
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.security.test.context.support.WithSecurityContext
import org.springframework.security.test.context.support.WithSecurityContextFactory
import java.lang.annotation.Inherited
import java.time.Instant
import java.time.temporal.ChronoUnit

/**
 *
 * @author Arthur Kazemi<bidadh@gmail.com>
 * @since 2019-01-11 21:03
 */

@kotlin.annotation.Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
@Inherited
@WithSecurityContext(factory = JwtAuthFactory::class)
annotation class WithMockJwt(val userId: String = "1", val name: String = "Name", val username: String = "username@example.com", val token: String = "token", val roles: Array<String> = ["ROLE_USER", "ROLE_ADMIN"])

class JwtAuthFactory : WithSecurityContextFactory<WithMockJwt> {
  override fun createSecurityContext(withUser: WithMockJwt): SecurityContext {

    val map = mutableMapOf(
        "user" to UserInfo(withUser.userId, withUser.name, withUser.username) as Any)
    val claims: MutableMap<String, Any> = map
    val headers: MutableMap<String, Any> = map

    val jwt = Jwt(withUser.token, Instant.now(), Instant.now().plus(365, ChronoUnit.DAYS), headers, claims)

    val authorities = withUser.roles.map { SimpleGrantedAuthority(it) }

    val authentication: AbstractAuthenticationToken = JwtAuthenticationToken(jwt, authorities)

    val context = SecurityContextHolder.createEmptyContext()
    context.authentication = authentication
    return context
  }
}