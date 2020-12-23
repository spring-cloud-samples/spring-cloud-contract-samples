package com.example;

import java.net.URI;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.UriBuilder;

import com.example.beerapiproducerjaxrs.FraudDetectionController;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.client.ClientConfig;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import static org.springframework.util.SocketUtils.findAvailableTcpPort;

/**
 * Example of using pure Jersey / Jetty API / no Spring to setup the tests.
 */
public class BeerRestBase {
	public static WebTarget webTarget;

	private static Server server;

	private static Client client;

	@BeforeAll
	public static void setupTest() throws Exception {
		int port = findAvailableTcpPort(10000);
		URI baseUri = UriBuilder.fromUri("http://localhost").port(port).build();
		// Create Server
		server = new Server(port);
		// Configure ServletContextHandler
		ServletContextHandler context = new ServletContextHandler(
				ServletContextHandler.SESSIONS);
		context.setContextPath("/");
		server.setHandler(context);
		// Create Servlet Container
		ServletHolder jerseyServlet = context
				.addServlet(org.glassfish.jersey.servlet.ServletContainer.class, "/*");
		jerseyServlet.setInitOrder(0);
		// Tells the Jersey Servlet which REST service/class to load.
		jerseyServlet.setInitParameter("jersey.config.server.provider.classnames",
				FraudDetectionController.class.getCanonicalName());
		// Start the server
		server.start();
		ClientConfig clientConfig = new ClientConfig();
		client = ClientBuilder.newClient(clientConfig);
		webTarget = client.target(baseUri);
		try {
			server.start();
		}
		catch (Exception ex) {
			throw new IllegalStateException(ex);
		}
	}

	@AfterAll
	public static void cleanupTest() {
		if (client != null) {
			client.close();
		}
		if (server != null) {
			try {
				server.stop();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
