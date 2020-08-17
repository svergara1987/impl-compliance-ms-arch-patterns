package uy.fing.edu.svergara.xml2junit.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "initialisation")
@XmlAccessorType(XmlAccessType.FIELD)
public class Initialization {

	@XmlElement(name = "value")
	private List<Value> values = null;

	public List<Value> getValues() {
		if (values == null) {
			values = new ArrayList<>();
		}
		return values;
	}

	public void setValues(List<Value> values) {
		this.values = values;
	}

}
