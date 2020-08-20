package uy.fing.edu.svergara.xml2junit.model.freemarker;

import java.util.ArrayList;
import java.util.List;

public class Operation {

	private String name;
	private List<Parameter> parameters;
	@SuppressWarnings("unused")
	private List<String> parametersStrRep;

	public String getName() {
		return name;
	}

	public List<Parameter> getParameters() {
		if (parameters == null) {
			parameters = new ArrayList<>();
		}
		return parameters;
	}

	public List<String> getParametersStrRep() {
		List<String> parametersStrRep = new ArrayList<>();
		for (Parameter param : getParameters()) {
			parametersStrRep.add(param.getStrRep());
		}
		return parametersStrRep;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setParameters(List<Parameter> parameters) {
		this.parameters = parameters;
	}

}
