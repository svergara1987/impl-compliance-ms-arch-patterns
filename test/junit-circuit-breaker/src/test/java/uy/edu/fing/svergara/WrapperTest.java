package uy.edu.fing.svergara;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class WrapperTest extends TestCase {
	/**
	 * Create the test case
	 *
	 * @param testName name of the test case
	 */
	public WrapperTest(String testName) {
		super(testName);
	}

//	https://stackoverflow.com/questions/38781398/test-drive-hystrix-circuit-breaker-configuration

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(WrapperTest.class);
	}

	public void testCase_1() {
		// case setup
		Integer THRESHOLD = 3;
		Integer AMOUNT_TEST_REQUESTS = 3;
		Integer TIMEOUT_PERIOD = 2;
		Integer test_request_to_go = 3;
		Integer timestamp_cb_trips = 0;
		Integer consecutive_errors = 0;
		circuit_breaker circuit_breaker = uy.edu.fing.svergara.circuit_breaker.CLOSED;
		Integer time = 0;
		Boolean microservice_response = null;
		// case execution
		Wrapper app = new Wrapper();
		app.initialisation(THRESHOLD, AMOUNT_TEST_REQUESTS, TIMEOUT_PERIOD, test_request_to_go, timestamp_cb_trips, consecutive_errors, circuit_breaker, time);
		// step 1
		microservice_response = false;
		app.request(microservice_response);
		consecutive_errors = 1;
		assertTrue(app.isValid(circuit_breaker, time, test_request_to_go, timestamp_cb_trips, consecutive_errors));
		// step 2
		microservice_response = false;
		app.request(microservice_response);
		consecutive_errors = 2;
		assertTrue(app.isValid(circuit_breaker, time, test_request_to_go, timestamp_cb_trips, consecutive_errors));
		// step 3
		microservice_response = false;
		app.request(microservice_response);
		consecutive_errors = 3;
		circuit_breaker = uy.edu.fing.svergara.circuit_breaker.OPEN;
		assertTrue(app.isValid(circuit_breaker, time, test_request_to_go, timestamp_cb_trips, consecutive_errors));
		// step 4
		app.clock();
		time = 1;
		assertTrue(app.isValid(circuit_breaker, time, test_request_to_go, timestamp_cb_trips, consecutive_errors));
		// step 5
		microservice_response = false;
		app.request(microservice_response);
		consecutive_errors = 0;
		assertTrue(app.isValid(circuit_breaker, time, test_request_to_go, timestamp_cb_trips, consecutive_errors));
	}

	public void testCase_2() {
		// case setup
		Integer THRESHOLD = 3;
		Integer AMOUNT_TEST_REQUESTS = 3;
		Integer TIMEOUT_PERIOD = 2;
		Integer test_request_to_go = 3;
		Integer timestamp_cb_trips = 0;
		Integer consecutive_errors = 0;
		circuit_breaker circuit_breaker = uy.edu.fing.svergara.circuit_breaker.CLOSED;
		Integer time = 0;
		Boolean microservice_response = null;
		// case execution
		Wrapper app = new Wrapper();
		app.initialisation(THRESHOLD, AMOUNT_TEST_REQUESTS, TIMEOUT_PERIOD, test_request_to_go, timestamp_cb_trips, consecutive_errors, circuit_breaker, time);
		// step 1
		microservice_response = true;
		app.request(microservice_response);
		assertTrue(app.isValid(circuit_breaker, time, test_request_to_go, timestamp_cb_trips, consecutive_errors));
	}

	public void testCase_3() {
		// case setup
		Integer THRESHOLD = 3;
		Integer AMOUNT_TEST_REQUESTS = 3;
		Integer TIMEOUT_PERIOD = 2;
		Integer test_request_to_go = 3;
		Integer timestamp_cb_trips = 0;
		Integer consecutive_errors = 0;
		circuit_breaker circuit_breaker = uy.edu.fing.svergara.circuit_breaker.CLOSED;
		Integer time = 0;
		Boolean microservice_response = null;
		// case execution
		Wrapper app = new Wrapper();
		app.initialisation(THRESHOLD, AMOUNT_TEST_REQUESTS, TIMEOUT_PERIOD, test_request_to_go, timestamp_cb_trips, consecutive_errors, circuit_breaker, time);
		// step 1
		app.clock();
		time = 1;
		assertTrue(app.isValid(circuit_breaker, time, test_request_to_go, timestamp_cb_trips, consecutive_errors));
	}
}
