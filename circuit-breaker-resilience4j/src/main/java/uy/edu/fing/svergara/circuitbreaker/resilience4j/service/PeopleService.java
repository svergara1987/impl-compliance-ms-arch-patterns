package uy.edu.fing.svergara.circuitbreaker.resilience4j.service;

import java.util.List;
import java.util.function.Supplier;

import org.springframework.http.ResponseEntity;

import uy.edu.fing.svergara.circuitbreaker.resilience4j.model.Person;

public interface PeopleService {

	public abstract Person addOrUpdateSkill(String email, String skillName, Integer skillValue);

	public abstract Person delete(String email);

	public abstract List<Person> list();

	public abstract Person register(Person person);

	public abstract Person retrieve(String email);

	public abstract Person update(String email, Person person);

	public abstract Supplier<ResponseEntity<Object>> listSuppplier(Integer delayInMilliseconds, Boolean fail);
}
