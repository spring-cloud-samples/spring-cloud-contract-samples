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
		body(fileAsBytes("PersonToCheck_too_young.bin"))
		headers {
			contentType("application/x-protobuf")
		}
	}
	response {
		status 200
		body(fileAsBytes("Response_too_young.bin"))
		headers {
			contentType("application/x-protobuf")
		}
	}
}