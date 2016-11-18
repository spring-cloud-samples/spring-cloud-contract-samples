package com.example;

import org.springframework.cloud.contract.spec.internal.ServerDslProperty;

/**
 * @author Marcin Grzejszczak
 */
public class ProducerUtils {
	public static ServerDslProperty ok() {
		return new ServerDslProperty("OK");
	}
}
