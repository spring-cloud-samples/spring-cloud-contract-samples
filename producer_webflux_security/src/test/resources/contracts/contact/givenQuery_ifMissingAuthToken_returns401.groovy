package contracts.contact

import org.springframework.cloud.contract.spec.Contract

/**
 *
 * @author Arthur Kazemi<bidadh@gmail.com>
 * @since 2019-06-23 22:58
 */
Contract.make {
    description("contact search when user not authorized responds 401")

    request {
        method(GET())
        headers {
            header(accept(), applicationJson())
        }
        url('/contact/search')
    }
    response {
        status(UNAUTHORIZED())
    }
}