package uy.fing.edu.svergara.xml2junit.model.testgenstrategyyaml;

public class Variable {

	private Boolean ignore;
	private String name;
	private String type;

	public Boolean getIgnore() {
		if (ignore == null) {
			setIgnore(false);
		}
		return ignore;
	}

	public String getName() {
		return name;
	}

	public String getType() {
		if (type == null) {
			setType(String.class.getSimpleName());
		}
		return type;
	}

	public Variable setIgnore(Boolean ignore) {
		this.ignore = ignore;
		return this;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "Variable [ignore=" + getIgnore() + ", name=" + getName() + ", type=" + getType() + "]";
	}
}
