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
//tag::impl[]
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
	 * That way it's in the implementation that we decide what value we will pass to the consumer
	 * and which one to the producer.
	 */
	public static ServerDslProperty ok() {
		// this example is not the best one and
		// theoretically you could just pass the regex instead of `ServerDslProperty` but
		// it's just to show some new tricks :)
		return new ServerDslProperty( PatternUtils.ok(), "OK");
	}
}
//end::impl[]