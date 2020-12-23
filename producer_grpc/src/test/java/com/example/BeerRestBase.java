package com.example;

//remove::start[]
import net.devh.boot.grpc.server.config.GrpcServerProperties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.verifier.http.HttpVerifier;
import org.springframework.cloud.contract.verifier.http.OkHttpHttpVerifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@SpringBootTest(classes = BeerRestBase.Config.class,
		webEnvironment = SpringBootTest.WebEnvironment.NONE,
		properties = {
				"grpc.server.port=0"
		})
//remove::end[]
public abstract class BeerRestBase {
	//remove::start[]

	@Autowired
	GrpcServerProperties properties;

	@Configuration
	@EnableAutoConfiguration
	static class Config {

		@Bean
		ProducerController producerController(PersonCheckingService personCheckingService) {
			return new ProducerController(personCheckingService);
		}

		@Bean
		PersonCheckingService testPersonCheckingService() {
			return argument -> argument.getAge() >= 20;
		}


		@Bean
		HttpVerifier httpOkVerifier(GrpcServerProperties properties) {
			return new OkHttpHttpVerifier("localhost:" + properties.getPort());
		}
	}

	//remove::end[]
}



