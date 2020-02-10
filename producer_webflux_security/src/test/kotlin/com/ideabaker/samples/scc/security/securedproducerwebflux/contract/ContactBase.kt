package com.ideabaker.samples.scc.security.securedproducerwebflux.contract

import com.ideabaker.samples.scc.security.securedproducerwebflux.config.GlobalSecurityConfig
import com.ideabaker.samples.scc.security.securedproducerwebflux.config.SpringSecurityWebFluxConfig
import com.ideabaker.samples.scc.security.securedproducerwebflux.model.UserContact
import com.ideabaker.samples.scc.security.securedproducerwebflux.service.ContactServiceProvider
import com.ideabaker.samples.scc.security.securedproducerwebflux.web.ContactHandler
import com.ideabaker.samples.scc.security.securedproducerwebflux.web.Routes
import io.restassured.module.webtestclient.RestAssuredWebTestClient
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import reactor.core.publisher.Flux

/**
 *
 * @author Arthur Kazemi<bidadh@gmail.com>
 * @since 2019-06-23 22:41
 */
@SpringBootTest(classes = [GlobalSecurityConfig::class, SpringSecurityWebFluxConfig::class, ContactBase.Config::class, Routes::class],
    webEnvironment = SpringBootTest.WebEnvironment.MOCK)
abstract class ContactBase {
  @Autowired
  lateinit var context: ApplicationContext

  @BeforeEach
  fun setup() {
    RestAssuredWebTestClient.applicationContextSetup(this.context)
  }

  @Configuration
  @EnableAutoConfiguration
  class Config {
    @Bean
    @Primary
    fun contactHandler(contactService: ContactServiceProvider): ContactHandler {
      return ContactHandler(contactService = contactService)
    }

    @Bean
    @Primary
    fun contactService(): ContactServiceProvider {
      return MockContactService()
    }
  }

  internal class MockContactService : ContactServiceProvider {
    override fun contactSearch(pattern: String): Flux<UserContact> {
      if (pattern == "existing") {
        val contact1 = UserContact("1", "name 1", "existing1@email.com", true)
        val contact2 = UserContact("2", "name 2", "existing2@email.com", false)
        return Flux.fromArray(arrayOf(contact1, contact2))
      }

      return Flux.empty()
    }
  }
}