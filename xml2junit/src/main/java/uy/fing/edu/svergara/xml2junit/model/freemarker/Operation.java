package uy.fing.edu.svergara.xml2junit.model.freemarker;

import java.util.ArrayList;
import java.util.List;

public class Operation {

	private String name;
	private List<Parameter> parameters;
	@SuppressWarnings("unused")
	private List<String> parametersStrRep;
	private String returnType;

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

	public String getReturnType() {
		if (returnType == null) {
			returnType = "void";
		}
		return returnType;
	}

	public Operation setName(String name) {
		this.name = name;
		return this;
	}

	public Operation setParameters(List<Parameter> parameters) {
		this.parameters = parameters;
		return this;
	}

	public Operation setReturnType(String returnType) {
		this.returnType = returnType;
		return this;
	}

	@Override
	public String toString() {
		return "Operation [name=" + getName() + ", parameters=" + getParameters() + ", parametersStrRep="
				+ getParametersStrRep() + ", returnType=" + getReturnType() + "]";
	}

}
