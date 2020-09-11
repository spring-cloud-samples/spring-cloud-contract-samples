package com.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

public class PersonToCheckService {

	private Map<String, PersonToCheck> db = new HashMap<>();

	public PersonToCheckService() {
		db.put("1", new PersonToCheck("Old Enough", 40));
		db.put("2", new PersonToCheck("Too Young", 10));
	}

	public PersonToCheck getCustomerByName(String id) {
		return db.entrySet().stream().filter(entry -> entry.getValue().getName().equals(id)).findFirst().map(Map.Entry::getValue).orElse(null);
	}

	public List<PersonToCheck> getAll() {
		return new ArrayList<>(db.values());
	}
}
