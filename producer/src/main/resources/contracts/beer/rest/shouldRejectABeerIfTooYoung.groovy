org.springframework.cloud.contract.spec.Contract.make {
    description("""
        Should Reject
    """)
    request {
        method POST()
        url "/check"
        body(
                age: 17, name: "marcin"
        )
        headers {
            contentType(applicationJson())
        }
    }
    response {
        status 200
        body("""
            { "status" : "NOT_OK" }
        """)
        headers {
            contentType(applicationJson())
        }
    }
}