package uy.edu.fing.svergara.circuitbreaker.resilience4j.exception;

public class PersonNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -9009644560434012556L;
	private String email;

	public PersonNotFoundException(String email) {
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

}
