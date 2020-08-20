package uy.fing.edu.svergara.xml2junit.model.testgenstrategyyaml;

import java.util.List;

public class Type {

	private String name;
	private String supertype;
	private List<String> values;

	public String getName() {
		return name;
	}

	public String getSupertype() {
		return supertype;
	}

	public List<String> getValues() {
		return values;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setSupertype(String supertype) {
		this.supertype = supertype;
	}

	public void setValues(List<String> values) {
		this.values = values;
	}
}
