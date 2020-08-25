package uy.fing.edu.svergara.xml2junit.model.freemarker;

import java.util.ArrayList;
import java.util.List;

public class JunitTestCase {

	private static Integer TEST_CASE_ID = 1;

	private Integer id;
	private List<JunitVariable> initializationVariables = null;
	private List<JunitStep> steps = null;

	public Integer getId() {
		if (id == null) {
			id = TEST_CASE_ID++;
		}
		return id;
	}

	public List<JunitVariable> getInitializationVariables() {
		if (initializationVariables == null) {
			initializationVariables = new ArrayList<>();
		}
		return initializationVariables;
	}

	public List<JunitStep> getSteps() {
		if (steps == null) {
			steps = new ArrayList<>();
		}
		return steps;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setInitializationVariables(List<JunitVariable> initializationVariables) {
		this.initializationVariables = initializationVariables;
	}

	public void setSteps(List<JunitStep> steps) {
		this.steps = steps;
	}
}
