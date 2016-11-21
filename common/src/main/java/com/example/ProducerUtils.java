package com.example;

import org.springframework.cloud.contract.spec.internal.ServerDslProperty;

/**
 * DSL Properties passed to the DSL from the producer's perspective.
 * That means that on the input side {@code Request} for HTTP
 * or {@code Input} for messaging you have to have a concrete value.
 * On the {@code Response} for HTTP or {@code Output} for messaging
 * you can have a regular expression.
 *
 * @author Marcin Grzejszczak
 */
public class ProducerUtils {

	/**
	 * Producer side property. By using the {@link ProducerUtils}
	 * you can omit most of boilerplate code from the perspective
	 * of dynamic values. Example
	 *
	 * <pre>
	 * {@code
	 * response {
	 *     body(
	 *         [ status: $(ProducerUtils.ok())]
	 *     )
	 * }
	 * </pre>
	 *
	 * That way the producer side value of age field will be
	 * a regular expression and the consumer side will be generated.
	 */
	public static ServerDslProperty ok() {
		return new ServerDslProperty(PatternUtils.ok());
	}
}
