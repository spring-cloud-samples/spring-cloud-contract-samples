package com.example;

//remove::start[]

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import com.example.beerapiproducerjaxrs.ProducerWithJaxRsApplication;
import org.junit.Before;
import org.junit.runner.RunWith;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

//remove::end[]
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ProducerWithJaxRsApplication.class},
		webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class BeerRestBase {
	//remove::start[]

	@LocalServerPort int port;

	public WebTarget webTarget;

	@Before
	public void setup() {
		this.webTarget = ClientBuilder.newClient().target("http://localhost:" + this.port + "/");
	}
	//remove::end[]
}
