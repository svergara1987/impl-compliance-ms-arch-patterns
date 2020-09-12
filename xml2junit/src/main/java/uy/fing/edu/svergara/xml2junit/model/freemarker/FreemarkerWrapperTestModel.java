package uy.fing.edu.svergara.xml2junit.model.freemarker;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import uy.fing.edu.svergara.xml2junit.model.testgenstrategyyaml.Type;

public class FreemarkerWrapperTestModel {

	private static final String ENUM = "enum";
	private List<String> isValidParameters = null;
	private List<Type> newTypes = null;
	private String packageName;
	private List<JunitTestCase> testCases = null;

	public String buildRightSideAssignment(String type, String value) {
		if (isEnum(type)) {
			return type + "." + value;
		} else if (isNewType(type)) {
			return type + ".parse(\"" + value + "\")";
		} else {
			return value;
		}
	}

	public List<String> getIsValidParameters() {
		if (isValidParameters == null) {
			isValidParameters = new ArrayList<>();
		} else {
			isValidParameters.sort(new Comparator<String>() {

				@Override
				public int compare(String o1, String o2) {
					return o1.compareTo(o2);
				}
			});
		}
		return isValidParameters;
	}

	public List<Type> getNewTypes() {
		if (newTypes == null) {
			newTypes = new ArrayList<>();
		}
		return newTypes;
	}

	public String getPackageName() {
		return packageName;
	}

	public List<JunitTestCase> getTestCases() {
		if (testCases == null) {
			testCases = new ArrayList<>();
		}
		return testCases;
	}

	public Boolean isAlreadyDefined(JunitTestCase testCase, Integer stepId, String variableName) {
		for (JunitVariable aVariable : testCase.getInitializationVariables()) {
			if (aVariable.getName().equalsIgnoreCase(variableName)) {
				return true;
			}
		}
		for (JunitStep aJunitStep : testCase.getSteps()) {
			if (stepId.equals(aJunitStep.getId())) {
				break;
			} else {
				for (JunitVariable aVariable : aJunitStep.getPre()) {
					if (aVariable.getName().equalsIgnoreCase(variableName)) {
						return true;
					}
				}
				for (JunitVariable aVariable : aJunitStep.getPost()) {
					if (aVariable.getName().equalsIgnoreCase(variableName)) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	private boolean isEnum(String type) {
		if (type == null)
			return false;
		for (Type aType : getNewTypes()) {
			if (type.equalsIgnoreCase(aType.getName())) {
				return ENUM.equalsIgnoreCase(aType.getSupertype());
			}
		}
		return false;
	}

	private boolean isNewType(String type) {
		for (Type aNewType : getNewTypes()) {
			if (aNewType.getName().equalsIgnoreCase(type)) {
				return true;
			}
		}
		return false;
	}

	public void setIsValidParameters(List<String> isValidParameters) {
		this.isValidParameters = isValidParameters;
	}

	public void setNewTypes(List<Type> newTypes) {
		this.newTypes = newTypes;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public void setTestCases(List<JunitTestCase> testCases) {
		this.testCases = testCases;
	}

	@Override
	public String toString() {
		return "FreemarkerWrapperTestModel [isValidParameters=" + getIsValidParameters() + ", newTypes=" + getNewTypes()
				+ ", packageName=" + getPackageName() + ", testCases=" + getTestCases() + "]";
	}

}
