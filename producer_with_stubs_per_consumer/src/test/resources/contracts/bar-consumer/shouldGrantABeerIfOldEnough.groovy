package contracts.beer.rest

import org.springframework.cloud.contract.spec.Contract

Contract.make {
	request {
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
		method 'POST'
		url '/check'
		body(
				age: $(regex("[2-9][0-9]")),
				name: "marcin"
		)
		headers {
			contentType(applicationJson())
		}
	}
	response {
		status OK()
		body("""
			{
				"status": "OK",
				"surname": "marcin"
			}
			""")
		headers {
			contentType(applicationJson())
		}
	}
}
