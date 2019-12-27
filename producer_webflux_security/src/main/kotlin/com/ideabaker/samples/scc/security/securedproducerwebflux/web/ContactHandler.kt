package com.ideabaker.samples.scc.security.securedproducerwebflux.web

import com.ideabaker.samples.scc.security.securedproducerwebflux.service.ContactServiceProvider
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

/**
 *
 * @author Arthur Kazemi<bidadh@gmail.com>
 * @since 2019-05-23 13:38
 */
@Service
class ContactHandler @Autowired constructor(private val contactService: ContactServiceProvider) {
  companion object {
    const val MIN_PATTERN_LENGTH = 4
  }

  private val logger = LoggerFactory.getLogger(ContactHandler::class.java)

  fun search(request: ServerRequest): Mono<ServerResponse> {
    val query = request.queryParam("query").orElse("")
    if (query.trim().length < MIN_PATTERN_LENGTH) {
      logger.debug("querying for pattern with less than $MIN_PATTERN_LENGTH characters responds NO_CONTENT")
      return ServerResponse.noContent().build()
    }

    logger.info("querying users for '{}'", query)
    return contactService.contactSearch(query)
        .collectList()
        .doOnError {
          logger.error("unable to search users: '${it.localizedMessage}'")
          logger.error("error: {}", it)
        }
        .flatMap {
          logger.info("'{}' users found for pattern: [}'", it.size, query)
          ServerResponse.ok().body(BodyInserters.fromValue(it))
        }
  }
}