package contracts.beer.rest

import com.example.PatternUtils
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
		stubMatchers {
			jsonPath('$.age', byRegex(PatternUtils.tooYoung()))
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