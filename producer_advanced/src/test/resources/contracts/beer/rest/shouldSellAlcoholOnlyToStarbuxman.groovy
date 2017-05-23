package contracts.beer.rest

import org.springframework.cloud.contract.spec.Contract

Contract.make {
	description("""
Represents a grumpy waiter that will sell alcohol only to Starbuxman.
""")
	request {
		method POST()
		url '/buy'
		body(
				name: "starbuxman",
				age: 25
		)
		stubMatchers {
			jsonPath('$.age', byRegex('[2-9][0-9]'))
		}
		headers {
			contentType(applicationJson())
		}
	}
	response {
		status 200
		body(
				message: "There you go Josh!",
				status: "OK"
		)
		headers {
			contentType(applicationJson())
		}
		async()
	}
	priority 10
}
