package uy.fing.edu.svergara.xml2junit.model.freemarker;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import uy.fing.edu.svergara.xml2junit.XML2Junit;
import uy.fing.edu.svergara.xml2junit.model.testcasesxml.ExtendedTestSuite;
import uy.fing.edu.svergara.xml2junit.model.testcasesxml.Modified;
import uy.fing.edu.svergara.xml2junit.model.testcasesxml.Step;
import uy.fing.edu.svergara.xml2junit.model.testcasesxml.TestCase;
import uy.fing.edu.svergara.xml2junit.model.testcasesxml.Value;
import uy.fing.edu.svergara.xml2junit.model.testgenstrategyyaml.TestGenStrategy;
import uy.fing.edu.svergara.xml2junit.model.testgenstrategyyaml.Type;
import uy.fing.edu.svergara.xml2junit.model.testgenstrategyyaml.Variable;

public class FreeMarkerModelBuilderFactory {

	private static final String INITALISATION = "initialisation";
	private static final FreeMarkerModelBuilderFactory INSTANCE = new FreeMarkerModelBuilderFactory();
	private static final String IS_VALID_OPERATION_NAME = "isValid";
	private final static Logger logger = Logger.getLogger(XML2Junit.class.getSimpleName());
	private static final String VARIABLE = "variable";

	public static FreeMarkerModelBuilderFactory instance() {
		return INSTANCE;
	}

	public FreemarkerEnumModel buildFreemarkerEnumrDataModel(TestGenStrategy testGenStrategy, Type aType) {
		FreemarkerEnumModel freemarkerModel = new FreemarkerEnumModel();
		freemarkerModel.setPackageName(testGenStrategy.getGroupId());
		freemarkerModel.setEnumName(aType.getName());
		freemarkerModel.setEnumValues(aType.getValues());
		logger.finest(freemarkerModel.toString());
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

	public FreemarkerWrapperTestModel buildFreemarkerWrapperTestDataModel(TestGenStrategy testGenStrategy,
			ExtendedTestSuite extendedTestSuite, FreemarkerWrapperModel freemarkerWrapperModel) {
		FreemarkerWrapperTestModel freemarkerModel = new FreemarkerWrapperTestModel();
		freemarkerModel.setPackageName(testGenStrategy.getGroupId());
		freemarkerModel.getNewTypes().addAll(testGenStrategy.getTypes());
		findIsValidOperation(freemarkerWrapperModel).getParameters().forEach((parameter) -> {
			freemarkerModel.getIsValidParameters().add(parameter.getName());
		});
		Map<String, Variable> variableMap = testGenStrategy.buildVariablesMap();
		JunitTestCase junitTestCase = null;
		JunitStep junitStep = null;
		for (TestCase aTestCase : extendedTestSuite.getTestCases()) {
			junitTestCase = new JunitTestCase();
			freemarkerModel.getTestCases().add(junitTestCase);
			junitStep = new JunitStep().setMethodName(INITALISATION);
			junitTestCase.getSteps().add(junitStep);
			for (Value aValue : aTestCase.getInitialisation().getValues()) {
				if (variableMap.containsKey(aValue.getName())) {
					if (!variableMap.get(aValue.getName()).getIgnore()) {
						junitTestCase.getInitializationVariables().add(new JunitVariable().setName(aValue.getName())
								.setType(variableMap.get(aValue.getName()).getType()).setValue(aValue.getValue()));
					}
				} else {
					junitTestCase.getInitializationVariables().add(new JunitVariable().setName(aValue.getName())
							.setType(String.class.getSimpleName()).setValue(aValue.getValue()));
				}
				junitStep.getMethodParameters().add(aValue.getName());
			}
			for (Step aStep : aTestCase.getSteps()) {
				junitStep = new JunitStep().setMethodName(aStep.getName());
				junitTestCase.getSteps().add(junitStep);
				for (Value aValue : aStep.getValues()) {
					if (!variableMap.get(aValue.getName()).getIgnore()) {
						junitStep.getPre().add(new JunitVariable().setName(aValue.getName())
								.setType(variableMap.get(aValue.getName()).getType()).setValue(aValue.getValue()));
						junitStep.getMethodParameters().add(aValue.getName());
					}
				}
				for (Modified aValue : aStep.getModifieds()) {
					if (!variableMap.get(aValue.getName()).getIgnore()) {
						junitStep.getPost().add(new JunitVariable().setName(aValue.getName())
								.setType(variableMap.get(aValue.getName()).getType()).setValue(aValue.getValue()));
					}
				}

			}
		}
		return freemarkerModel;
	}

	private Operation buildIsValidOperation(Step initialisation, Map<String, Variable> variableMap) {
		Operation isValid = new Operation().setName(IS_VALID_OPERATION_NAME)
				.setReturnType(Boolean.class.getSimpleName());
		for (Value aValue : initialisation.getValues()) {
			if (VARIABLE.equalsIgnoreCase(aValue.getType())) {
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
				stepParams.add(new Parameter().setName(aValue.getName()).setType(String.class.getSimpleName()));
			}
		}
		return anOperation;
	}

	private Operation findIsValidOperation(FreemarkerWrapperModel freemarkerWrapperModel) {
		for (Operation operation : freemarkerWrapperModel.getOperations()) {
			if (IS_VALID_OPERATION_NAME.equals(operation.getName())) {
				return operation;
			}
		}
		throw new RuntimeException(IS_VALID_OPERATION_NAME + " operation does not exists");
	}
}
