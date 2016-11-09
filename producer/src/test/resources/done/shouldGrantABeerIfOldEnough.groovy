org.springframework.cloud.contract.spec.Contract.make {
	request {
		method 'POST'
		url '/check'
		body(
			age: value(consumer(regex('[2-9][0-9]')))
		)
		headers {
			header 'Content-Type', 'application/json'
		}
	}
	response {
		status 200
		body( """
			{
				"status": "OK"
			}
			""")
		headers {
			header(
				'Content-Type', value(consumer('application/json'),producer(regex('application/json.*')))
			)
		}
	}
}
