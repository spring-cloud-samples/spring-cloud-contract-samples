package com.ideabaker.samples.scc.security.securedproducerwebflux.service

import com.ideabaker.samples.scc.security.securedproducerwebflux.model.UserContact
import org.slf4j.LoggerFactory
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux

/**
 *
 * @author Arthur Kazemi<bidadh@gmail.com>
 * @since 2019-05-23 05:22
 */
@Service
class ContactService : ContactServiceProvider {
  @PreAuthorize("isAuthenticated()")
  override fun contactSearch(pattern: String): Flux<UserContact> {
    logger.info("Search for '{}'", pattern)
    return Flux.empty()
  }

  internal val logger = LoggerFactory.getLogger(ContactService::class.java)
}

interface ContactServiceProvider {
  fun contactSearch(pattern: String): Flux<UserContact>
}