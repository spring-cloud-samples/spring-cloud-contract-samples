package contracts.contact.secured

import org.springframework.cloud.contract.spec.Contract

/**
 *
 * @author Arthur Kazemi<bidadh@gmail.com>
 * @since 24/12/19 18:45
 */
Contract.make {
  description("contact search returns 200")

  request {
    method(GET())
    headers {
      header(authorization(), $(c('FAKE_AUTH')))
      header(accept(), applicationJson())
    }
    urlPath('/contact/search') {
      queryParameters {
        parameter 'query': equalTo('existing')
      }
    }
  }
  response {
    status(OK())
    headers {
      header(contentType(), applicationJson())
    }
    body([
        [
            id     : "1",
            name   : "name 1",
            email  : "existing1@email.com",
            invited: true
        ],
        [
            id     : "2",
            name   : "name 2",
            email  : "existing2@email.com",
            invited: false
        ]
    ])
  }
}