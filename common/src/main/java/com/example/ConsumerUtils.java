package com.example;

import org.springframework.cloud.contract.spec.internal.ClientDslProperty;
import org.springframework.cloud.contract.spec.internal.DslProperty;

/**
 * DSL Properties passed to the DSL from the consumer's perspective.
 * That means that on the input side {@code Request} for HTTP
 * or {@code Input} for messaging you can have a regular expression.
 * On the {@code Response} for HTTP or {@code Output} for messaging
 * you have to have a concrete value.
 *
 * @author Marcin Grzejszczak
 */
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
	 * That way the consumer side value of age field will be
	 * a regular expression and the producer side will be generated.
	 *
	 * @author Marcin Grzejszczak
	 */
	public static ClientDslProperty oldEnough() {
		return new ClientDslProperty(PatternUtils.oldEnough());
	}

	/**
	 * Consumer side property. By using the {@link ClientDslProperty}
	 * you can omit most of boilerplate code from the perspective
	 * of dynamic values. Example
	 *
	 * <pre>
	 * {@code
	 * request {
	 *     body(
	 *         [ name: $(ConsumerUtils.anyName())]
	 *     )
	 * }
	 * </pre>
	 *
	 * That way the consumer will be a regular expression and the
	 * producer side value will be equal to {@code marcin}
	 */
	public static DslProperty anyName() {
		return new DslProperty<>(PatternUtils.anyName(), "marcin");
	}
}
