package contracts.beer.rest

import org.springframework.cloud.contract.spec.Contract

Contract.make {
	request {
		method GET()
		url '/stout'
	}
	response {
		status 200
		body("STOUT")
		headers {
			contentType(textPlain())
		}
	}
}
