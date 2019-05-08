package contracts.beer.rest


import com.example.ProducerUtils
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
		headers {
			contentType(applicationJson())
			header(authorization(), "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwiaWF0IjoxNTE2MjM5MDIyLCJ1c2VyX2RldGFpbHMiOnsidXNlcm5hbWUiOiJKb2huIERvZSIsImFnZSI6NDB9fQ.qPcKwEerTbAT8KvByyB__lCxR_ujuzitzD5clECyndY")
		}
	}
	response {
		status 200
		body("""
			{
				"status": "${value(ProducerUtils.ok())}"
			}
			""")
		headers {
			contentType(applicationJson())
		}
	}
}
