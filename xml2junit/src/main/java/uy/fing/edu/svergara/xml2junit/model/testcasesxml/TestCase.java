package uy.fing.edu.svergara.xml2junit.model.testcasesxml;

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
	private Step initialisation;
	@XmlElement(name = "step")
	private List<Step> steps = null;

	public Step getInitialisation() {
		initialisation.setName("initialisation");
		return initialisation;
	}

	public List<Step> getSteps() {
		if (steps == null) {
			steps = new ArrayList<>();
		}
		return steps;
	}

	public void setInitialisation(Step initialisation) {
		this.initialisation = initialisation;
	}

	public void setSteps(List<Step> steps) {
		this.steps = steps;
	}

}
