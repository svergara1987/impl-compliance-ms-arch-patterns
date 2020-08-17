package uy.fing.edu.svergara.xml2junit.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

@XmlRootElement(name = "value")
@XmlAccessorType(XmlAccessType.FIELD)
public class Value {

	@XmlAttribute
	private String name;
	@XmlAttribute
	private String type;
	@XmlValue
	private String value;

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public String getValue() {
		return value;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
