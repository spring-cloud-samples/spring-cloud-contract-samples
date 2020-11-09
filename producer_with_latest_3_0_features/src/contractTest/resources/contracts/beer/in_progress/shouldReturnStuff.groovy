package contracts.beer.rest

import org.springframework.cloud.contract.spec.Contract

Contract.make {
	// will not generate tests, will allow to generate stubs
	inProgress()
	request {
		method 'GET'
		url '/stuff'
	}
	response {
		status 200
		body("OK")
	}
}
