package uy.edu.fing.svergara.circuitbreaker.hystrix.exception;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NullParametersException extends RuntimeException {

	private static final long serialVersionUID = 2257620542611838429L;

	private List<String> parameters;

	public NullParametersException(String... parameters) {
		this.parameters = Arrays.asList(parameters);
	}

	public List<String> getParameters() {
		if (parameters == null) {
			parameters = new ArrayList<>();
		}
		return parameters;
	}

}
