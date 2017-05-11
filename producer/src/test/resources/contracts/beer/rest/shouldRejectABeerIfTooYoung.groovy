package contracts.beer.rest

import org.springframework.cloud.contract.spec.Contract

import static com.example.PatternUtils.tooYoung

Contract.make {
	description("""
Represents a unsuccessful scenario of getting a beer

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
		stubMatchers {
			jsonPath('$.age', byRegex(tooYoung()))
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
		testMatchers {
			jsonPath('$.status', byType())
		}
	}
}