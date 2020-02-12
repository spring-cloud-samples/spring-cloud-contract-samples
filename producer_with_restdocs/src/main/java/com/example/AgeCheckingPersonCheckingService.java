package com.example;

import com.example.model.PersonToCheck;
import com.example.model.Verification;
import reactor.core.publisher.EmitterProcessor;

import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

/**
 * @author Marcin Grzejszczak
 */
@Service
public class AgeCheckingPersonCheckingService implements PersonCheckingService {

	private final EmitterProcessor<Message<Verification>> source;

	public AgeCheckingPersonCheckingService(EmitterProcessor<Message<Verification>> source) {
		this.source = source;
	}

	@Override
	public Boolean shouldGetBeer(PersonToCheck personToCheck) {
		//remove::start[]
		boolean shouldGetBeer = personToCheck.age >= 20;
		this.source.onNext(MessageBuilder.withPayload(new Verification(shouldGetBeer)).build());
		return shouldGetBeer;
		//remove::end[return]
	}

}
