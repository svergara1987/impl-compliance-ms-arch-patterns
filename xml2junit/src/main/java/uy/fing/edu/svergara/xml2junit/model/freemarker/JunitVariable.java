package uy.fing.edu.svergara.xml2junit.model.freemarker;

public class JunitVariable {

	private String name;
	private String type;
	private String value;

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public String getValue() {
		if (Boolean.class.getSimpleName().equalsIgnoreCase(getType())) {
			value = String.valueOf(Boolean.getBoolean(value));
		}
		return value;
	}

	public JunitVariable setName(String name) {
		this.name = name;
		return this;
	}

	public JunitVariable setType(String type) {
		this.type = type;
		return this;
	}

	public JunitVariable setValue(String value) {
		this.value = value;
		return this;
	}

}
