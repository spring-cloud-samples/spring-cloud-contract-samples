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
		headers {
			contentType(applicationJson())
			header(authorization(), "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwiaWF0IjoxNTE2MjM5MDIyLCJ1c2VyX2RldGFpbHMiOnsidXNlcm5hbWUiOiJKb2huIERvZSIsImFnZSI6MTZ9fQ.algTE74sdM0Y9oawjdWZ-syvolJxxYor_eQVNmVTwOs")
		}
	}
	response {
		status 200
		body("""
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