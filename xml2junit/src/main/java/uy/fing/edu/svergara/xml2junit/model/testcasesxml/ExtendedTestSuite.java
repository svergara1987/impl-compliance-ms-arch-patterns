package uy.fing.edu.svergara.xml2junit.model.testcasesxml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "extended_test_suite")
@XmlAccessorType(XmlAccessType.FIELD)
public class ExtendedTestSuite {

	@XmlElement(name = "test_case")
	private List<TestCase> testCases = null;

	public List<TestCase> getTestCases() {
		if (testCases == null) {
			testCases = new ArrayList<>();
		}
		return testCases;
	}

	public void setTestCases(List<TestCase> testCases) {
		this.testCases = testCases;
	}

}
