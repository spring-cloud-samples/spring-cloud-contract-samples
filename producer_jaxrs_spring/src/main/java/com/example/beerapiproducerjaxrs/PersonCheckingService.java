package com.example.beerapiproducerjaxrs;

import com.example.beerapiproducerjaxrs.model.FraudCheck;

public interface PersonCheckingService {
	Boolean shouldGetBeer(FraudCheck fraudCheck);
}