package com.example.service;

import com.example.security.core.userdetails.UserDetails;

public class PersonCheckingService {

    public Boolean shouldGetBeer(UserDetails userDetails) {
        return userDetails.getAge() >= 21;
    }
}
