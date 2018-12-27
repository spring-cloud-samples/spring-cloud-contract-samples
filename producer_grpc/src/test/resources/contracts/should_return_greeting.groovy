import org.springframework.cloud.contract.spec.Contract

Contract.make {
	description("Should work with GRPC")
	request {
		url("/com.example.grpc.HelloService/SayHello")
		method POST()
		headers {
			contentType("application/grpc+proto")
			header("grpc-encoding", "gzip")
		}
		body(fileAsBytes("request.bin"))
	}
	response {
		status OK()
		headers {
			contentType("application/grpc+proto")
			header("grpc-encoding", "gzip")
		}
		body(fileAsBytes("reply.bin"))
	}
}