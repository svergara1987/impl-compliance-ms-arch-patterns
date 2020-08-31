package uy.edu.fing.svergara.circuitbreaker.resilience4j.model;

import java.util.HashMap;
import java.util.Map;

public class Person {

	private String email;
	private Map<String, Integer> assessedSkills;

	public String getEmail() {
		return email;
	}

	public Person setEmail(String email) {
		this.email = email;
		return this;
	}

	public Map<String, Integer> getAssessedSkills() {
		if (assessedSkills == null) {
			assessedSkills = new HashMap<>();
		}
		return assessedSkills;
	}

	public Person setAssessedSkills(Map<String, Integer> assessedSkills) {
		this.assessedSkills = assessedSkills;
		return this;
	}

}
