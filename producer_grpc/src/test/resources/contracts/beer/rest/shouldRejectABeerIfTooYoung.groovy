package contracts.beer.rest

import org.springframework.cloud.contract.spec.Contract
import org.springframework.cloud.contract.verifier.http.ContractVerifierHttpMetaData

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
		url '/beer.BeerService/check'
		body(fileAsBytes("PersonToCheck_too_young.bin"))
		headers {
			contentType("application/grpc")
			header("te", "trailers")
		}
	}
	response {
		status 200
		body(fileAsBytes("Response_too_young.bin"))
		headers {
			contentType("application/grpc")
			header("grpc-encoding", "identity")
			header("grpc-accept-encoding", "gzip")
		}
	}
	metadata([
			"verifierHttp": [
					"protocol": ContractVerifierHttpMetaData.Protocol.H2_PRIOR_KNOWLEDGE.toString()
			]
	])
}