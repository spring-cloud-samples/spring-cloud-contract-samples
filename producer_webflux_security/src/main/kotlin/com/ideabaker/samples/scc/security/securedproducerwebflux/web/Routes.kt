package com.ideabaker.samples.scc.security.securedproducerwebflux.web

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.router


/**
 *
 * @author Arthur Kazemi<bidadh@gmail.com>
 * @since 2019-01-07 01:36
 */

@Configuration
class Routes {
  @Bean("contactRouter")
  fun contactRouter(contactHandler: ContactHandler) = router {
    GET("/contact/search") { contactHandler.search(it) }
  }
}
