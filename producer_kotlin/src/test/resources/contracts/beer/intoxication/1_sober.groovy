package contracts.beer.intoxication

import org.springframework.cloud.contract.spec.Contract

Contract.make {
	description("""
Represents first step of getting fully drunk

given:
	you didn't drink anything
when:
	you get a beer
then:
	you'll be tipsy
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
				previousStatus: "SOBER",
				currentStatus: "TIPSY"
		)
		headers {
			contentType(applicationJson())
		}
	}
}
