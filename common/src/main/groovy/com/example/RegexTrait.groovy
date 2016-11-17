package com.example

import java.util.regex.Pattern

/**
 * @author Marcin Grzejszczak
 */
trait RegexTrait {

	Pattern tooYoung() {
		return Pattern.compile("[0-1][0-9]")
	}
}