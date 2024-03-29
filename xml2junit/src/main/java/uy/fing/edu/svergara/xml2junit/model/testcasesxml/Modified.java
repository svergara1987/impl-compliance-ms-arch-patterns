package uy.fing.edu.svergara.xml2junit.model.testcasesxml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

@XmlRootElement(name = "modified")
@XmlAccessorType(XmlAccessType.FIELD)
public class Modified {

	@XmlAttribute
	private String name;
	@XmlValue
	private String value;

	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "Modified [name=" + getName() + ", value=" + getValue() + "]";
	}

}
