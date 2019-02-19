package contracts.beer.rest


import org.springframework.cloud.contract.spec.Contract

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
		body(fileAsBytes("PersonToCheck_old_enough.bin"))
		headers {
			contentType("application/x-protobuf")
		}
	}
	response {
		status 200
		body(fileAsBytes("Response_old_enough.bin"))
		headers {
			contentType("application/x-protobuf")
		}
	}
}
