package com.example;

import org.springframework.cloud.contract.spec.internal.ClientDslProperty;

/**
 * @author Marcin Grzejszczak
 */
public class ConsumerUtils {
	public static ClientDslProperty oldEnough() {
		return new ClientDslProperty(PatternUtils.oldEnough());
	}
}
