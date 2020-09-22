import org.springframework.cloud.contract.spec.Contract

Contract.make {
	label("trigger")
	input {
		triggeredBy("trigger()")
	}
	outputMessage {
		sentTo("DEV.QUEUE.1")
		headers {
			header("hello", "world")
		}
		body([
		        foo: "example"
		])
	}
}