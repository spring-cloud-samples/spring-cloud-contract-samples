package com.ideabaker.samples.scc.security.securedproducerwebflux.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter
import org.springframework.util.FileCopyUtils
import java.security.KeyFactory
import java.security.interfaces.RSAPublicKey
import java.security.spec.X509EncodedKeySpec
import java.util.*

/**
 *
 * @author Arthur Kazemi<bidadh@gmail.com>
 * @since 2019-01-20 14:55
 */
@Configuration
class GlobalSecurityConfig {
  @Bean
  fun passwordEncoder(): BCryptPasswordEncoder {
    return BCryptPasswordEncoder()
  }

  @Value("public.txt")
  lateinit var resource: ClassPathResource

  @Bean
  fun jwtAuthoritiesAwareAuthConverter() = JwtAuthoritiesAuthenticationConverter(JwtAuthenticationConverter())

  @Bean
  fun publicKey(): RSAPublicKey {
    var publicKey = String(FileCopyUtils.copyToByteArray(resource.inputStream))
    publicKey = publicKey.replace("[\\r\\n]".toRegex(), "").replace("-----BEGIN PUBLIC KEY-----", "").replace("-----END PUBLIC KEY-----", "")
    val kf = KeyFactory.getInstance("RSA")
    val keySpecX509 = X509EncodedKeySpec(Base64.getDecoder().decode(publicKey))

    return kf.generatePublic(keySpecX509) as RSAPublicKey
  }
}