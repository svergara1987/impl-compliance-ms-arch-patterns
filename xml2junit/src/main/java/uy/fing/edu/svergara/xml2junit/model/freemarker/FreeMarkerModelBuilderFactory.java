package uy.fing.edu.svergara.xml2junit.model.freemarker;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import uy.fing.edu.svergara.xml2junit.model.testcasesxml.ExtendedTestSuite;
import uy.fing.edu.svergara.xml2junit.model.testcasesxml.Step;
import uy.fing.edu.svergara.xml2junit.model.testcasesxml.TestCase;
import uy.fing.edu.svergara.xml2junit.model.testcasesxml.Value;
import uy.fing.edu.svergara.xml2junit.model.testgenstrategyyaml.TestGenStrategy;
import uy.fing.edu.svergara.xml2junit.model.testgenstrategyyaml.Type;
import uy.fing.edu.svergara.xml2junit.model.testgenstrategyyaml.Variable;

public class FreeMarkerModelBuilderFactory {

	private static final FreeMarkerModelBuilderFactory INSTANCE = new FreeMarkerModelBuilderFactory();

	public static FreeMarkerModelBuilderFactory instance() {
		return INSTANCE;
	}

	public FreemarkerEnumModel buildFreemarkerEnumrDataModel(TestGenStrategy testGenStrategy, Type aType) {
		FreemarkerEnumModel freemarkerModel = new FreemarkerEnumModel();
		freemarkerModel.setPackageName(testGenStrategy.getGroupId());
		freemarkerModel.setEnumName(aType.getName());
		freemarkerModel.setEnumValues(aType.getValues());
		return freemarkerModel;
	}

	public FreemarkerWrapperModel buildFreemarkerWrapperDataModel(TestGenStrategy testGenStrategy,
			ExtendedTestSuite extendedTestSuite) {
		FreemarkerWrapperModel freemarkerModel = new FreemarkerWrapperModel();
		freemarkerModel.setPackageName(testGenStrategy.getGroupId());
		Map<String, Variable> variableMap = testGenStrategy.buildVariablesMap();
		Set<String> operations = new HashSet<>();
		for (TestCase aTestCase : extendedTestSuite.getTestCases()) {
			if (!operations.contains(aTestCase.getInitialisation().getName())) {
				operations.add(aTestCase.getInitialisation().getName());
				freemarkerModel.getOperations().add(buildOperation(aTestCase.getInitialisation(), variableMap));
				freemarkerModel.getOperations().add(buildIsValidOperation(aTestCase.getInitialisation(), variableMap));
			}
			for (Step aStep : aTestCase.getSteps()) {
				if (!operations.contains(aStep.getName())) {
					operations.add(aStep.getName());
					freemarkerModel.getOperations().add(buildOperation(aStep, variableMap));
				}
			}
		}
		return freemarkerModel;
	}

	public FreemarkerWrapperModel buildFreemarkerWrapperTestDataModel(TestGenStrategy testGenStrategy,
			ExtendedTestSuite extendedTestSuite) {
		// TODO FALTA ARMARLO
		return null;
	}

	private Operation buildIsValidOperation(Step initialisation, Map<String, Variable> variableMap) {
		Operation isValid = new Operation().setName("isValid").setReturnType(Boolean.class.getSimpleName());
		for (Value aValue : initialisation.getValues()) {
			if ("variable".equalsIgnoreCase(aValue.getType())) {
				if (variableMap.containsKey(aValue.getName())) {
					if (!variableMap.get(aValue.getName()).getIgnore()) {
						isValid.getParameters().add(new Parameter().setName(aValue.getName())
								.setType(variableMap.get(aValue.getName()).getType()));
					}
				} else {
					// defaults to String if not defined
					isValid.getParameters()
							.add(new Parameter().setName(aValue.getName()).setType(String.class.getSimpleName()));
				}
			}
		}
		return isValid;

	}

	private Operation buildOperation(Step step, Map<String, Variable> variableMap) {
		Operation anOperation = new Operation();
		anOperation.setName(step.getName());
		List<Parameter> stepParams = anOperation.getParameters();
		List<Value> stepValues = step.getValues();
		for (Value aValue : stepValues) {
			if (variableMap.containsKey(aValue.getName())) {
				if (!variableMap.get(aValue.getName()).getIgnore()) {
					stepParams.add(new Parameter().setName(aValue.getName())
							.setType(variableMap.get(aValue.getName()).getType()));
				}
			} else {
				// defaults to String if not defined
				stepParams.add(new Parameter().setName(aValue.getName()).setType("String"));
			}
		}
		return anOperation;
	}
}
