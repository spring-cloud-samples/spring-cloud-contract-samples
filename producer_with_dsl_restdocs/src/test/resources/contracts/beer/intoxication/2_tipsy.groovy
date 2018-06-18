package contracts.beer.intoxication

import org.springframework.cloud.contract.spec.Contract

Contract.make {
	description("""
Represents second step of getting fully drunk

given:
	you were tipsy
when:
	you get a beer
then:
	you'll be drunk
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
				previousStatus: "TIPSY",
				currentStatus: "DRUNK"
		)
		headers {
			contentType(applicationJson())
		}
	}
}
