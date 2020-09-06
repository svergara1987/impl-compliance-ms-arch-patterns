package uy.edu.fing.svergara.serviceregistry.eureka.service;

import java.util.List;

import uy.edu.fing.svergara.serviceregistry.eureka.model.Person;

public interface PeopleService {

	public abstract Person addOrUpdateSkill(String email, String skillName, Integer skillValue);

	public abstract Person delete(String email);

	public abstract List<Person> list();

	public abstract Person register(Person person);

	public abstract Person retrieve(String email);

	public abstract Person update(String email, Person person);
}
