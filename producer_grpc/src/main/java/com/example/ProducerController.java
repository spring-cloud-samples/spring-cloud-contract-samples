package com.example;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@GrpcService
public class ProducerController extends BeerServiceGrpc.BeerServiceImplBase {

	private static final Logger log = LoggerFactory.getLogger(ProducerController.class);

	private final PersonCheckingService personCheckingService;

	public ProducerController(PersonCheckingService personCheckingService) {
		this.personCheckingService = personCheckingService;
	}

	@Override
	public void check(PersonToCheck request, StreamObserver<Response> responseObserver) {
		log.info("Age Received : " + request.getAge());
		Response.BeerCheckStatus status = personCheckingService.shouldGetBeer(request) ? Response.BeerCheckStatus.OK : Response.BeerCheckStatus.NOT_OK;
		Response response = Response.newBuilder().setStatus(status).build();
		responseObserver.onNext(response);
		responseObserver.onCompleted();
	}
}

interface PersonCheckingService {
	boolean shouldGetBeer(PersonToCheck request);
}