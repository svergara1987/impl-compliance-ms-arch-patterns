package uy.fing.edu.svergara.xml2junit.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "test_case")
@XmlAccessorType(XmlAccessType.FIELD)
public class TestCase {

	@XmlElement(name = "initialisation")
	private Initialization initialization;
	@XmlElement(name = "step")
	private List<Step> steps = null;

	public Initialization getInitialization() {
		return initialization;
	}

	public List<Step> getSteps() {
		if (steps == null) {
			steps = new ArrayList<>();
		}
		return steps;
	}

	public void setInitialization(Initialization initialization) {
		this.initialization = initialization;
	}

	public void setSteps(List<Step> steps) {
		this.steps = steps;
	}

}
