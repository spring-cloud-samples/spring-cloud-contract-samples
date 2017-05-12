package contracts.beer.rest

import org.springframework.cloud.contract.spec.Contract

import static com.example.ConsumerUtils.oldEnough
import static com.example.ProducerUtils.ok

Contract.make {
	description("""
Represents a successful scenario of getting a beer

```
given:
	client is old enough
when:
	he applies for a beer
then:
	we'll grant him the beer
```

""")
	request {
		method 'POST'
		url '/check'
		body(
				age: $(oldEnough())
		)
		headers {
			contentType(applicationJson())
		}
	}
	response {
		status 200
		body("""
			{
				"status": "${value(ok())}"
			}
			""")
		headers {
			contentType(applicationJson())
		}
	}
}
