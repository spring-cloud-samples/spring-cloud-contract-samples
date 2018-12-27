package com.example;

import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.lognet.springboot.grpc.context.LocalRunningGrpcPort;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SampleGrcpApplication.class,
		webEnvironment = SpringBootTest.WebEnvironment.NONE,
		properties = {
			"grpc.port=0", "grpc.enableReflection=true"
		})
public abstract class BaseClass {

	@LocalRunningGrpcPort
	int port;

	@Before
	public void setup() {
		System.err.println("GRPC running at port [" + this.port + "]");
		RestAssured.basePath = "https://localhost:" + this.port;
	}
}
