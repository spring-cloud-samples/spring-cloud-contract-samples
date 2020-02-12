package contracts.beer.rest

import org.springframework.cloud.contract.spec.Contract

Contract.make {
	description("""
Represents a scenario in which a user asks for his drinking stats.

```
given:
	any client
when:
	he asks for stats
then:
	we'll send him back his stats
and:
	we'll prepare a personalized text to show the user
```

""")
	request {
		method 'POST'
		url '/stats'
		body(
				name: anyAlphaUnicode()
		)
		headers {
			contentType(applicationJson())
		}
	}
	response {
		status 200
		body(
				text: "Dear ${fromRequest().body('$.name')} thanks for your interested in drinking beer",
				quantity: $(c(5), p(anyNumber()))
		)
		headers {
			contentType(applicationJson())
		}
	}
}
