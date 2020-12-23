package com.example;

import java.util.List;

import graphql.schema.DataFetchingEnvironment;
import graphql.schema.PropertyDataFetcher;

import org.springframework.stereotype.Component;

public class PeopleToCheckDataFetcher extends PropertyDataFetcher<List<PersonToCheck>> {

	private final PersonToCheckService personToCheckService;

	public PeopleToCheckDataFetcher(PersonToCheckService personToCheckService) {
		super("peopleToCheck");
		this.personToCheckService = personToCheckService;
	}

	@Override
	public List<PersonToCheck> get(DataFetchingEnvironment environment) {
		return personToCheckService.getAll();
	}
}
