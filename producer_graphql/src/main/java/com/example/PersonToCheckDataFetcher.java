package com.example;

import graphql.schema.DataFetchingEnvironment;
import graphql.schema.PropertyDataFetcher;

public class PersonToCheckDataFetcher extends PropertyDataFetcher<PersonToCheck> {

	private final PersonToCheckService personToCheckService;

	public PersonToCheckDataFetcher(PersonToCheckService personToCheckService) {
		super("personToCheck");
		this.personToCheckService = personToCheckService;
	}

	@Override
	public PersonToCheck get(DataFetchingEnvironment environment) {
		String name = environment.getArgument("name");
		return personToCheckService.getCustomerByName(name);
	}
}
