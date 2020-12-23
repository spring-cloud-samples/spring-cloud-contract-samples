package com.ideabaker.samples.scc.security.consumerwebflux

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerPort
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient

/**
 *
 * @author Arthur Kazemi<bidadh@gmail.com>
 * @since 24/12/19 23:21
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@AutoConfigureStubRunner(
    ids = ["com.example:secured-producer-webflux"],
    stubsMode = StubRunnerProperties.StubsMode.LOCAL
)
@AutoConfigureJsonTesters
class ContactConsumerTest {
  lateinit var webTestClient: WebTestClient

  @StubRunnerPort("secured-producer-webflux") lateinit var port: Integer

  @BeforeEach
  fun before() {
    webTestClient = WebTestClient
        .bindToServer()
        .baseUrl("http://localhost:" + this.port)
        .build()
  }

  @Test
  fun givenUnauthorizedUser_whenSearchContacts_shouldRespond401() {
    webTestClient
        .get()
        .uri("/contact/search")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus()
        .isEqualTo(HttpStatus.UNAUTHORIZED)
  }

  @Test
  fun givenAuthorizedUser_whenSearchContacts_shouldRespondOK() {
    webTestClient
        .get()
        .uri("/contact/search?query=existing")
        .accept(MediaType.APPLICATION_JSON)
        .header(HttpHeaders.AUTHORIZATION, "FAKE_AUTH")
        .exchange()
        .expectStatus()
        .isEqualTo(HttpStatus.OK)
    //TODO: validate body here!
  }
}