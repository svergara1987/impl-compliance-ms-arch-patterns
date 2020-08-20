package uy.fing.edu.svergara.xml2junit.model.freemarker;

public class Parameter {

	private String name;
	private String type;

	public String getName() {
		return name;
	}

	public String getStrRep() {
		return getType() + " " + getName();
	}

	public String getType() {
		return type;
	}

	public Parameter setName(String name) {
		this.name = name;
		return this;
	}

	public Parameter setType(String type) {
		this.type = type;
		return this;
	}

}
