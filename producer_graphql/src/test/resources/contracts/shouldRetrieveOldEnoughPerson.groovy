import org.springframework.cloud.contract.spec.Contract

Contract.make {

	request {
		method(POST())
		url("/graphql")
		headers {
			contentType("application/json")
		}
		body('''
{
	"query":"query queryName($personName: String!) {\\n  personToCheck(name: $personName) {\\n    name\\n    age\\n  }\\n}\\n\\n\\n\\n",
	"variables":{"personName":"Old Enough"},
	"operationName":"queryName"
}
''')
	}
	
	response {
		status(200)
		headers {
			contentType("application/json")
		}
		body('''\
{
  "data": {
    "personToCheck": {
      "name": "Old Enough",
      "age": "40"
    }
  }
}
''')
	}
	metadata(verifier: [
	        tool: "graphql"
	])

}