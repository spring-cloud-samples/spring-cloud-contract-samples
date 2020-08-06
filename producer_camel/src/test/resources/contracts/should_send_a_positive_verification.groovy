import org.springframework.cloud.contract.spec.Contract

Contract.make {
	label("positive")
	input {
		messageFrom("seda:person")
		messageBody([
		        age: 25
		])
		messageHeaders {
			messagingContentType(applicationJson())
		}
	}
	outputMessage {
		sentTo("seda:verifications")
		body([
		        eligible: true
		])
		headers {
			messagingContentType(applicationJson())
		}
	}
}