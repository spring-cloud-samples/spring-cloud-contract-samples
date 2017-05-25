org.springframework.cloud.contract.spec.Contract.make {
    description("""
        Should Grant
    """)
    request {
        method POST()
        url "/check"
        body(
                age: 22, name: "marcin"
        )
        headers {
            contentType(applicationJson())
        }
    }
    response {
        status 200
        body("""
            { "status" : "OK" }
        """)
        headers {
            contentType(applicationJson())
        }
    }
}