package contracts.beer.rest

org.springframework.cloud.contract.spec.Contract.make {
	request {
		description("""
Represents a successful scenario of getting a beer

given:
	client is old enough
when:
	he applies for a beer
then:
	we'll grant him the beer
""")
		method ''
		url ''
		body(
			[:]
		)
		headers {
			header 'Content-Type', 'application/json'
		}
	}
	response {
		status 200
		body( """

			""")
		headers {
			header(
					'Content-Type', value(consumer('application/json'),producer(regex('application/json.*')))
			)
		}
	}
}
