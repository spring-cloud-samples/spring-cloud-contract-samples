package com.example;

import com.example.model.PersonToCheck;
import com.example.model.Verification;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

/**
 * @author Marcin Grzejszczak
 */
@Service
public class AgeCheckingPersonCheckingService implements PersonCheckingService {

	private final StreamBridge source;

	public AgeCheckingPersonCheckingService(StreamBridge source) {
		this.source = source;
	}

	@Override
	public Boolean shouldGetBeer(PersonToCheck personToCheck) {
		//remove::start[]
		boolean shouldGetBeer = personToCheck.age >= 20;
		this.source.send("output-out-0", new Verification(shouldGetBeer));
		return shouldGetBeer;
		//remove::end[return]
	}

}
