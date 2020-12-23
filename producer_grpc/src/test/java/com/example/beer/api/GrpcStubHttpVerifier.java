package com.example.beer.api;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.asarkar.grpc.test.Resources;
import com.example.BeerServiceGrpc;
import com.example.PersonToCheck;
import com.google.protobuf.InvalidProtocolBufferException;
import io.grpc.ManagedChannel;
import io.grpc.Status;
import io.grpc.StatusException;
import io.grpc.StatusRuntimeException;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.RequestBody;

import org.springframework.cloud.contract.verifier.http.HttpVerifier;
import org.springframework.cloud.contract.verifier.http.Request;
import org.springframework.cloud.contract.verifier.http.Response;

/**
 * Example of how you can use the HttpVerifier with stubbed HTTP/2 communication.
 */
public class GrpcStubHttpVerifier implements HttpVerifier {

	private final ManagedChannel channel;

	public GrpcStubHttpVerifier(ManagedChannel channel) {
		this.channel = channel;
	}

	@Override
	public Response exchange(Request request) {
		if (request.path().contains("beer.BeerService/check")) {
			try {
				com.example.Response response = BeerServiceGrpc.newBlockingStub(channel).check(PersonToCheck.parseFrom(request.body().asByteArray()));
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				response.writeTo(stream);
				return Response
						.builder()
						.body(stream.toByteArray())
						.header("Content-Type", "application/grpc")
						.statusCode(200)
						.build();
			}
			catch (IOException e) {
				throw new IllegalStateException(e);
			}
		}
		throw new UnsupportedOperationException("Request with path [" + request.path() + "] is unsupported");
	}
}
