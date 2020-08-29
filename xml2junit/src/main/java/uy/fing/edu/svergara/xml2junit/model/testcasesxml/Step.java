package uy.fing.edu.svergara.xml2junit.model.testcasesxml;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "step")
@XmlAccessorType(XmlAccessType.FIELD)
public class Step {

	@XmlElement(name = "modified")
	private List<Modified> modifieds = null;
	@XmlAttribute
	private String name;
	@XmlElement(name = "value")
	private List<Value> values = null;

	public List<Modified> getModifieds() {
		if (modifieds == null) {
			modifieds = new ArrayList<>();
		}
		return modifieds;
	}

	public String getName() {
		return name;
	}

	public List<Value> getValues() {
		if (values == null) {
			values = new ArrayList<>();
		} else {
			values.sort(new Comparator<Value>() {

				@Override
				public int compare(Value v1, Value v2) {
					return v1.getName().compareTo(v2.getName());
				}
			});
		}
		return values;
	}

	public void setModifieds(List<Modified> modifieds) {
		if (modifieds == null) {
			modifieds = new ArrayList<>();
		}
		this.modifieds = modifieds;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setValues(List<Value> values) {
		this.values = values;
	}

	@Override
	public String toString() {
		return "Step [modifieds=" + getModifieds() + ", name=" + getName() + ", values=" + getValues() + "]";
	}

}
