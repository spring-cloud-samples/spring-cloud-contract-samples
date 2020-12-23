package com.example;


//remove::start[]
import javax.annotation.Nullable;
import javax.net.ssl.SSLException;

import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.ClientCall;
import io.grpc.ClientInterceptor;
import io.grpc.Metadata;
import io.grpc.MethodDescriptor;
import io.grpc.Status;
import io.grpc.netty.shaded.io.grpc.netty.GrpcSslContexts;
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;
import io.grpc.netty.shaded.io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import net.devh.boot.grpc.client.autoconfigure.GrpcClientAutoConfiguration;
import net.devh.boot.grpc.client.channelfactory.GrpcChannelConfigurer;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.cloud.contract.stubrunner.HttpServerStubConfiguration;
import org.springframework.cloud.contract.stubrunner.junit.StubRunnerExtension;
import org.springframework.cloud.contract.stubrunner.provider.wiremock.WireMockHttpServerStubConfigurer;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

/**
 * @author Marcin Grzejszczak
 */
@SpringBootTest(webEnvironment = WebEnvironment.NONE, classes = GrpcTests.TestConfiguration.class, properties = {
		"grpc.client.beerService.address=static://localhost:5433", "grpc.client.beerService.negotiationType=TLS"
})
// remove::end[]
public class GrpcTests {
	//remove::start[]

	@GrpcClient(value = "beerService", interceptorNames = "fixedStatusSendingClientInterceptor")
	BeerServiceGrpc.BeerServiceBlockingStub beerServiceBlockingStub;

	int port;

	@RegisterExtension
	static StubRunnerExtension rule = new StubRunnerExtension()
			.downloadStub("com.example", "beer-api-producer-grpc")
			// With WireMock PlainText mode
//			.withPort(5433)
			.stubsMode(StubRunnerProperties.StubsMode.LOCAL)
			.withHttpServerStubConfigurer(MyWireMockConfigurer.class);

	@BeforeAll
	public static void beforeClass() {
		Assumptions.assumeTrue(atLeast300(), "Spring Cloud Contract must be in version at least 3.0.0");
		Assumptions.assumeTrue(StringUtils.isEmpty(System.getenv("OLD_PRODUCER_TRAIN")),
				"Env var OLD_PRODUCER_TRAIN must not be set");
	}

	@BeforeEach
	public void setupPort() {
		this.port = rule.findStubUrl("beer-api-producer-grpc").getPort();
	}

	private static boolean atLeast300() {
		try {
			Class.forName("org.springframework.cloud.contract.verifier.dsl.wiremock.SpringCloudContractRequestMatcher");
		}
		catch (Exception ex) {
			return false;
		}
		return true;
	}

	// tag::tests[]
	@Test
	public void should_give_me_a_beer_when_im_old_enough() throws Exception {
		Response response = beerServiceBlockingStub.check(PersonToCheck.newBuilder().setAge(23).build());

		BDDAssertions.then(response.getStatus()).isEqualTo(Response.BeerCheckStatus.OK);
	}

	@Test
	public void should_reject_a_beer_when_im_too_young() throws Exception {
		Response response = beerServiceBlockingStub.check(PersonToCheck.newBuilder().setAge(17).build());
		// TODO: If someone knows how to do this properly for default responses that would be helpful
		response = response == null ? Response.newBuilder().build() : response;

		BDDAssertions.then(response.getStatus()).isEqualTo(Response.BeerCheckStatus.NOT_OK);
	}
	// end::tests[]

	// Not necessary with WireMock PlainText mode
	static class MyWireMockConfigurer extends WireMockHttpServerStubConfigurer {
		@Override
		public WireMockConfiguration configure(WireMockConfiguration httpStubConfiguration, HttpServerStubConfiguration httpServerStubConfiguration) {
			return httpStubConfiguration
					.httpsPort(5433);
		}
	}

	@Configuration
	@ImportAutoConfiguration(GrpcClientAutoConfiguration.class)
	static class TestConfiguration {

		// Not necessary with WireMock PlainText mode
		@Bean
		public GrpcChannelConfigurer keepAliveClientConfigurer() {
			return (channelBuilder, name) -> {
				if (channelBuilder instanceof NettyChannelBuilder) {
					try {
						((NettyChannelBuilder) channelBuilder)
								.sslContext(GrpcSslContexts.forClient()
										.trustManager(InsecureTrustManagerFactory.INSTANCE)
										.build());
					}
					catch (SSLException e) {
						throw new IllegalStateException(e);
					}
				}
			};
		}

		/**
		 * GRPC client interceptor that sets the returned status always to OK.
		 * You might want to change the return status depending on the received stub payload.
		 *
		 * Hopefully in the future this will be unnecessary and will be removed.
		 */
		@Bean
		ClientInterceptor fixedStatusSendingClientInterceptor() {
			return new ClientInterceptor() {
				@Override
				public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> method, CallOptions callOptions, Channel next) {
					ClientCall<ReqT, RespT> call = next.newCall(method, callOptions);
					return new ClientCall<ReqT, RespT>() {
						@Override
						public void start(Listener<RespT> responseListener, Metadata headers) {
							Listener<RespT> listener = new Listener<RespT>() {
								@Override
								public void onHeaders(Metadata headers) {
									responseListener.onHeaders(headers);
								}

								@Override
								public void onMessage(RespT message) {
									responseListener.onMessage(message);
								}

								@Override
								public void onClose(Status status, Metadata trailers) {
									// TODO: This must be fixed somehow either in Jetty (WireMock) or somewhere else
									responseListener.onClose(Status.OK, trailers);
								}

								@Override
								public void onReady() {
									responseListener.onReady();
								}
							};
							call.start(listener, headers);
						}

						@Override
						public void request(int numMessages) {
							call.request(numMessages);
						}

						@Override
						public void cancel(@Nullable String message, @Nullable Throwable cause) {
							call.cancel(message, cause);
						}

						@Override
						public void halfClose() {
							call.halfClose();
						}

						@Override
						public void sendMessage(ReqT message) {
							call.sendMessage(message);
						}
					};
				}
			};
		}
	}
	//remove::end[]
}

