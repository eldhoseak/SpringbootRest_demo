package com.spring.rest.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.spring.rest.model.Person;

@Service
public class PersonService {

	private Map<String, Person> personsDB = new HashMap<String, Person>();

	// save person details.
	public void addPerson(Person person){
		// for simplicity we are assigning the first name as key.
		this.personsDB.put(person.getFirstName(), person);
	}

	// return all persons
	public Collection<Person> getAllPersons() {

		return  personsDB.values();
	}

}
