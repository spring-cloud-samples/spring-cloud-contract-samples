import org.springframework.cloud.contract.spec.Contract

Contract.make {
	label("positive")
	input {
		triggeredBy("triggerMessage(25)")
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
