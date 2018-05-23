package com.example;

import org.springframework.stereotype.Service;

/**
 * @author Marcin Grzejszczak
 */
@Service
public class AgeCheckingPersonCheckingService implements PersonCheckingService {

	@Override
	public Boolean shouldGetBeer(PersonToCheck personToCheck) {
		return personToCheck.age >= 20;
	}
}
