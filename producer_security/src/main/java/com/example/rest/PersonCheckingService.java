package com.example.rest;

import com.example.security.UserDetails;

import org.springframework.stereotype.Component;

@Component
public class PersonCheckingService {

	public Boolean shouldGetBeer(UserDetails userDetails) {
		// Very complex logic, calling databases etc.
		return userDetails.getAge() >= 100;
	}

}
