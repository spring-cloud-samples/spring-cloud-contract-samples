package com.example;

import java.util.regex.Pattern;

/**
 * If you want to use {@link Pattern} directly in your tests
 * then you can create a class resembling this one. It can
 * contain all the {@link Pattern} you want to use in the DSL.
 *
 * <pre>
 * {@code
 * request {
 *     body(
 *         [ age: $(c(PatternUtils.oldEnough()))]
 *     )
 * }
 * </pre>
 *
 * Notice that we're using both {@code $()} for dynamic values
 * and {@code c()} for the consumer side.
 *
 * @author Marcin Grzejszczak
 */
public class PatternUtils {
	public static Pattern tooYoung() {
		return Pattern.compile("[0-1][0-9]");
	}

	public static Pattern oldEnough() {
		return Pattern.compile("[2-9][0-9]");
	}

	public static Pattern anyName() {
		return Pattern.compile("[a-zA-Z]+");
	}

	/**
	 * Makes little sense but it's just an example ;)
	 */
	public static Pattern ok() {
		return Pattern.compile("OK");
	}
}
