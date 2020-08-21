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
	
	public void testCase_1() {

}