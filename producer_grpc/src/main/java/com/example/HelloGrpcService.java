package com.example;

import com.example.grpc.HelloReply;
import com.example.grpc.HelloRequest;
import com.example.grpc.HelloServiceGrpc;
import io.grpc.stub.StreamObserver;
import org.lognet.springboot.grpc.GRpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of the server-side grpc HelloService.
 *
 * NOTE: GrpcService is a composite annotation that extends Service, so this class will be picked up in a component scan.
 *
 * @author tyler.vangorder
 *
 */
@GRpcService
public class HelloGrpcService extends HelloServiceGrpc.HelloServiceImplBase {

	private static final Logger logger = LoggerFactory.getLogger(HelloGrpcService.class);

	@Override
	public void sayHello(HelloRequest request, StreamObserver<HelloReply> responseObserver) {
		String message = "Hello " + request.getName();
		logger.info("In the grpc server stub.");
		HelloReply reply = HelloReply.newBuilder().setMessage(message).build();
		responseObserver.onNext(reply);
		responseObserver.onCompleted();
	}
}

