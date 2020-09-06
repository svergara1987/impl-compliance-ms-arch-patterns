package uy.edu.fing.svergara.serviceregistry.eureka.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import uy.edu.fing.svergara.serviceregistry.eureka.exception.NullParametersException;
import uy.edu.fing.svergara.serviceregistry.eureka.exception.PersonNotFoundException;
import uy.edu.fing.svergara.serviceregistry.eureka.model.Person;

@Service
public class PeopleServiceImpl implements PeopleService {

	private static Map<String, Person> PEOPLE_REPO = new HashMap<>();
	static {
		String email = "persona1@fing.edu.uy";
		PEOPLE_REPO.put(email, new Person().setEmail(email).setAssessedSkills(
				new HashMap<String, Integer>(Map.of("python", 4, "watson", 5, "eventb", 3, "spring", 2))));

		email = "persona2@fing.edu.uy";
		PEOPLE_REPO.put(email, new Person().setEmail(email).setAssessedSkills(
				new HashMap<String, Integer>(Map.of("python", 1, "watson", 2, "eventb", 5, "spring", 3))));
	}

	@Override
	public Person addOrUpdateSkill(String email, String skillName, Integer skillValue) {
		if (email == null || skillName == null || skillValue == null) {
			throw new NullParametersException("email", "skillName", "skillValue");
		} else if (!PEOPLE_REPO.containsKey(email)) {
			throw new PersonNotFoundException(email);
		} else {
			Person person = PEOPLE_REPO.get(email);
			person.getAssessedSkills().put(skillName, skillValue);
			return person;
		}
	}

	@Override
	public Person delete(String email) {
		if (email == null) {
			throw new NullParametersException("email");
		}
		return PEOPLE_REPO.remove(email);
	}

	@Override
	public List<Person> list() {
		return new ArrayList<>(PEOPLE_REPO.values());
	}

	@Override
	public Person register(Person person) {
		if (person == null || person.getEmail() == null) {
			throw new NullParametersException("person", "person.email");
		} else {
			if (PEOPLE_REPO.containsKey(person.getEmail())) {
				return update(person.getEmail(), person);
			} else {
				PEOPLE_REPO.put(person.getEmail(), person);
				return person;
			}
		}
	}

	@Override
	public Person retrieve(String email) {
		if (email == null) {
			throw new NullParametersException("email");
		} else {
			if (PEOPLE_REPO.containsKey(email)) {
				return PEOPLE_REPO.get(email);
			} else {
				throw new PersonNotFoundException(email);
			}
		}
	}

	@Override
	public Person update(String email, Person person) {
		if (email == null || person == null || person.getEmail() == null) {
			throw new NullParametersException("email", "person", "person.email");
		} else if (!PEOPLE_REPO.containsKey(email)) {
			throw new PersonNotFoundException(email);
		} else if (person.getEmail() != email) {
			PEOPLE_REPO.remove(email);
			return register(person);
		} else {
			PEOPLE_REPO.get(email).setAssessedSkills(person.getAssessedSkills());
			return person;
		}
	}

}
