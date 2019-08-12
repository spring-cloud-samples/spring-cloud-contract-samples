package contracts.beer.intoxication

import org.springframework.cloud.contract.spec.Contract

Contract.make {
	description("""
Represents last step of getting fully drunk

given:
	you were drunk
when:
	you get a beer
then:
	you'll be wasted
""")
	request {
		method 'POST'
		url '/beer'
		body(
				name: "marcin"
		)
		headers {
			contentType(applicationJson())
		}
	}
	response {
		status 200
		body(
				previousStatus: "DRUNK",
				currentStatus: "WASTED"
		)
		headers {
			contentType(applicationJson())
		}
	}
}
