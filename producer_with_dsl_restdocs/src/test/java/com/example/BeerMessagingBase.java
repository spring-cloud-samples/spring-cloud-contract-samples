package com.example;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ProducerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
public abstract class BeerMessagingBase {
	@Autowired PersonCheckingService personCheckingService;

	@Before
	public void setup() {
		// let's clear any remaining messages
		// output == destination or channel name
	}

	public void clientIsOldEnough() {
	}

	public void clientIsTooYoung() {
	}
}
