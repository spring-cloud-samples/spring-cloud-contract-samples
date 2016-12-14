package contracts.beer.rest

org.springframework.cloud.contract.spec.Contract.make {
	request {
		description("""
Represents a unsuccessful scenario of getting a beer

given:
	client is not old enough
when:
	he applies for a beer
then:
	we'll NOT grant him the beer
""")
		method ''
		url ''
		body(
		[:]
		)
		headers {
			contentType(applicationJson())
		}
	}
	response {
		status 200
		body( """
		""")
		headers {
			contentType(applicationJson())
		}
	}
}
