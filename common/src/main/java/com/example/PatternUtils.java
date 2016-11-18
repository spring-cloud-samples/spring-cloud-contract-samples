package com.example;

import java.util.regex.Pattern;

/**
 * @author Marcin Grzejszczak
 */
public class PatternUtils {
	public static Pattern tooYoung() {
		return Pattern.compile("[0-1][0-9]");
	}

	public static Pattern oldEnough() {
		return Pattern.compile("[2-9][0-9]");
	}
}
