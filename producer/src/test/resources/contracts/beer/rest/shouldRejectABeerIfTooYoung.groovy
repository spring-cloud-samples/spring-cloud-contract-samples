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
				age: $(c(tooYoung()))
		)
		headers {
			contentType(applicationJson())
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
			contentType(applicationJson())
		}
	}
}