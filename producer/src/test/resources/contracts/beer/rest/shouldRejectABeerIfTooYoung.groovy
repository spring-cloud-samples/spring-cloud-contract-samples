package contracts.beer.rest

import org.springframework.cloud.contract.spec.Contract

import static com.example.PatternUtils.tooYoung

Contract.make {
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
		method 'POST'
		url '/check'
		body(
				age: value(consumer(tooYoung()))
		)
		headers {
			header 'Content-Type', 'application/json'
		}
	}
	response {
		status 200
		body( """
	{
		"status": "NOT_OK"
	}
	""")
		headers {
			header(
					'Content-Type', value(consumer('application/json'),producer(regex('application/json.*')))
			)
		}
	}
}