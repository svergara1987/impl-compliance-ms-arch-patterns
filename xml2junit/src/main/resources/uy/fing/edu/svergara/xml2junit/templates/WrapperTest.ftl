package ${packageName};

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class WrapperTest extends TestCase {
	
	public WrapperTest(String testName) {
		super(testName);
	}

	public static Test suite() {
		return new TestSuite(WrapperTest.class);
	}
	
	<#list testCases as aTestCase>
	public void testCase_${aTestCase.id}() {
		// case setup
		<#list aTestCase.initializationVariables as anInitializationVariable>
		${anInitializationVariable.type} ${anInitializationVariable.name} = ${buildRightSideAssignment(anInitializationVariable.type, anInitializationVariable.value)};
		</#list>
		// case execution
		Wrapper app = new Wrapper();
		<#list aTestCase.steps as aStep>
		<#list aStep.pre as pre>
		<#if !isAlreadyDefined(aTestCase, aStep.id, pre.name)>${pre.type} </#if>${pre.name} = ${buildRightSideAssignment(pre.type, pre.value)};
		</#list>
		app.${aStep.methodName}(${aStep.methodParameters?join(", ")});
		<#list aStep.post as post>
		<#if !isAlreadyDefined(aTestCase, aStep.id, post.name)>${post.type} </#if>${post.name} = ${buildRightSideAssignment(post.type, post.value)};
		</#list>
		assertTrue(app.isValid(${isValidParameters?join(", ")}));
		</#list>
	}
	
	</#list>

}