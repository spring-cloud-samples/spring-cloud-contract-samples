package contracts.beer.rest

import org.springframework.cloud.contract.spec.Contract

Contract.make {
	description("""
Represents an unsuccessful scenario of getting a beer

```
given:
	client is not old enough
when:
	he applies for a beer
then:
	we'll NOT grant him the beer
```

""")
	request {
		method 'POST'
		url '/check'
		body(
				age: 10
		)
		headers {
			contentType(applicationJson())
		}
		bodyMatchers {
			jsonPath('$.age', byRegex("[0-1][0-9]"))
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
		bodyMatchers {
			jsonPath('$.status', byType())
		}
	}
}