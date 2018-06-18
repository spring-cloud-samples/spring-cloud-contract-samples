package com.example;

import org.springframework.cloud.contract.spec.internal.ClientDslProperty;

/**
 * DSL Properties passed to the DSL from the consumer's perspective.
 * That means that on the input side {@code Request} for HTTP
 * or {@code Input} for messaging you can have a regular expression.
 * On the {@code Response} for HTTP or {@code Output} for messaging
 * you have to have a concrete value.
 *
 * @author Marcin Grzejszczak
 */
//tag::impl[]
public class ConsumerUtils {
	/**
	 * Consumer side property. By using the {@link ClientDslProperty}
	 * you can omit most of boilerplate code from the perspective
	 * of dynamic values. Example
	 *
	 * <pre>
	 * {@code
	 * request {
	 *     body(
	 *         [ age: $(ConsumerUtils.oldEnough())]
	 *     )
	 * }
	 * </pre>
	 *
	 * That way it's in the implementation that we decide what value we will pass to the consumer
	 * and which one to the producer.
	 *
	 * @author Marcin Grzejszczak
	 */
	public static ClientDslProperty oldEnough() {
		//remove::start[]
		// this example is not the best one and
		// theoretically you could just pass the regex instead of `ServerDslProperty` but
		// it's just to show some new tricks :)
		return new ClientDslProperty(PatternUtils.oldEnough(), 40);
		//remove::end[return]
	}

}
//end::impl[]
