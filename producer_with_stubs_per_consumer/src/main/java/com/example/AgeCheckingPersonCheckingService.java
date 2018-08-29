package com.example;

import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

/**
 * @author Marcin Grzejszczak
 */
@Service
public class AgeCheckingPersonCheckingService implements PersonCheckingService {

	@Override
	public Boolean shouldGetBeer(PersonToCheck personToCheck) {
		return null;
	}

}
