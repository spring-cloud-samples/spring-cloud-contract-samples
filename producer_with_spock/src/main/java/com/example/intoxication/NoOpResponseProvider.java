package com.example.intoxication;

import org.springframework.stereotype.Service;

/**
 * @author Marcin Grzejszczak
 */
@Service
class NoOpResponseProvider implements ResponseProvider {
	@Override public Response thereYouGo(Customer personToCheck) {
		return null;
	}
}
