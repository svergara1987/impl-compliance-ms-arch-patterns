package uy.edu.fing.svergara;

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
		// case setup
		Integer AMOUNT_TEST_REQUESTS = 3;
		Integer THRESHOLD = 3;
		Integer TIMEOUT_PERIOD = 2;
		CircuitBreaker circuit_breaker = CircuitBreaker.CLOSED;
		Integer consecutive_errors = 0;
		Integer test_request_to_go = 3;
		Integer time = 0;
		Integer timestamp_cb_trips = 0;
		// case execution
		Wrapper app = new Wrapper();
		app.initialisation(AMOUNT_TEST_REQUESTS, THRESHOLD, TIMEOUT_PERIOD, circuit_breaker, consecutive_errors,
				test_request_to_go, time, timestamp_cb_trips);
		assertTrue(app.isValid(circuit_breaker, consecutive_errors, test_request_to_go, time, timestamp_cb_trips));
		Boolean microservice_response = false;
		app.request(microservice_response);
		consecutive_errors = 1;
		assertTrue(app.isValid(circuit_breaker, consecutive_errors, test_request_to_go, time, timestamp_cb_trips));
		microservice_response = false;
		app.request(microservice_response);
		consecutive_errors = 2;
		assertTrue(app.isValid(circuit_breaker, consecutive_errors, test_request_to_go, time, timestamp_cb_trips));
		microservice_response = false;
		app.request(microservice_response);
		consecutive_errors = 3;
		circuit_breaker = CircuitBreaker.OPEN;
		assertTrue(app.isValid(circuit_breaker, consecutive_errors, test_request_to_go, time, timestamp_cb_trips));
		app.clock();
		time = 1;
		assertTrue(app.isValid(circuit_breaker, consecutive_errors, test_request_to_go, time, timestamp_cb_trips));
		microservice_response = false;
		app.request(microservice_response);
		consecutive_errors = 0;
		assertTrue(app.isValid(circuit_breaker, consecutive_errors, test_request_to_go, time, timestamp_cb_trips));
	}

	public void testCase_2() {
		// case setup
		Integer AMOUNT_TEST_REQUESTS = 3;
		Integer THRESHOLD = 3;
		Integer TIMEOUT_PERIOD = 2;
		CircuitBreaker circuit_breaker = CircuitBreaker.CLOSED;
		Integer consecutive_errors = 0;
		Integer test_request_to_go = 3;
		Integer time = 0;
		Integer timestamp_cb_trips = 0;
		// case execution
		Wrapper app = new Wrapper();
		app.initialisation(AMOUNT_TEST_REQUESTS, THRESHOLD, TIMEOUT_PERIOD, circuit_breaker, consecutive_errors,
				test_request_to_go, time, timestamp_cb_trips);
		assertTrue(app.isValid(circuit_breaker, consecutive_errors, test_request_to_go, time, timestamp_cb_trips));
		Boolean microservice_response = true;
		app.request(microservice_response);
		assertTrue(app.isValid(circuit_breaker, consecutive_errors, test_request_to_go, time, timestamp_cb_trips));
	}

	public void testCase_3() {
		// case setup
		Integer AMOUNT_TEST_REQUESTS = 3;
		Integer THRESHOLD = 3;
		Integer TIMEOUT_PERIOD = 2;
		CircuitBreaker circuit_breaker = CircuitBreaker.CLOSED;
		Integer consecutive_errors = 0;
		Integer test_request_to_go = 3;
		Integer time = 0;
		Integer timestamp_cb_trips = 0;
		// case execution
		Wrapper app = new Wrapper();
		app.initialisation(AMOUNT_TEST_REQUESTS, THRESHOLD, TIMEOUT_PERIOD, circuit_breaker, consecutive_errors,
				test_request_to_go, time, timestamp_cb_trips);
		assertTrue(app.isValid(circuit_breaker, consecutive_errors, test_request_to_go, time, timestamp_cb_trips));
		app.clock();
		time = 1;
		assertTrue(app.isValid(circuit_breaker, consecutive_errors, test_request_to_go, time, timestamp_cb_trips));
	}

	public void testCase_4() {
		// case setup
		Integer AMOUNT_TEST_REQUESTS = 3;
		Integer THRESHOLD = 3;
		Integer TIMEOUT_PERIOD = 2;
		CircuitBreaker circuit_breaker = CircuitBreaker.CLOSED;
		Integer consecutive_errors = 0;
		Integer test_request_to_go = 3;
		Integer time = 0;
		Integer timestamp_cb_trips = 0;
		// case execution
		Wrapper app = new Wrapper();
		app.initialisation(AMOUNT_TEST_REQUESTS, THRESHOLD, TIMEOUT_PERIOD, circuit_breaker, consecutive_errors,
				test_request_to_go, time, timestamp_cb_trips);
		assertTrue(app.isValid(circuit_breaker, consecutive_errors, test_request_to_go, time, timestamp_cb_trips));
		Boolean microservice_response = false;
		app.request(microservice_response);
		consecutive_errors = 1;
		assertTrue(app.isValid(circuit_breaker, consecutive_errors, test_request_to_go, time, timestamp_cb_trips));
		microservice_response = false;
		app.request(microservice_response);
		consecutive_errors = 2;
		assertTrue(app.isValid(circuit_breaker, consecutive_errors, test_request_to_go, time, timestamp_cb_trips));
		microservice_response = false;
		app.request(microservice_response);
		circuit_breaker = CircuitBreaker.OPEN;
		consecutive_errors = 3;
		assertTrue(app.isValid(circuit_breaker, consecutive_errors, test_request_to_go, time, timestamp_cb_trips));
		app.clock();
		time = 1;
		assertTrue(app.isValid(circuit_breaker, consecutive_errors, test_request_to_go, time, timestamp_cb_trips));
		app.clock();
		circuit_breaker = CircuitBreaker.HALF_OPEN;
		time = 2;
		assertTrue(app.isValid(circuit_breaker, consecutive_errors, test_request_to_go, time, timestamp_cb_trips));
	}

	public void testCase_5() {
		// case setup
		Integer AMOUNT_TEST_REQUESTS = 3;
		Integer THRESHOLD = 3;
		Integer TIMEOUT_PERIOD = 2;
		CircuitBreaker circuit_breaker = CircuitBreaker.CLOSED;
		Integer consecutive_errors = 0;
		Integer test_request_to_go = 3;
		Integer time = 0;
		Integer timestamp_cb_trips = 0;
		// case execution
		Wrapper app = new Wrapper();
		app.initialisation(AMOUNT_TEST_REQUESTS, THRESHOLD, TIMEOUT_PERIOD, circuit_breaker, consecutive_errors,
				test_request_to_go, time, timestamp_cb_trips);
		assertTrue(app.isValid(circuit_breaker, consecutive_errors, test_request_to_go, time, timestamp_cb_trips));
		Boolean microservice_response = false;
		app.request(microservice_response);
		consecutive_errors = 1;
		assertTrue(app.isValid(circuit_breaker, consecutive_errors, test_request_to_go, time, timestamp_cb_trips));
		microservice_response = false;
		app.request(microservice_response);
		consecutive_errors = 2;
		assertTrue(app.isValid(circuit_breaker, consecutive_errors, test_request_to_go, time, timestamp_cb_trips));
		microservice_response = false;
		app.request(microservice_response);
		circuit_breaker = CircuitBreaker.OPEN;
		consecutive_errors = 3;
		assertTrue(app.isValid(circuit_breaker, consecutive_errors, test_request_to_go, time, timestamp_cb_trips));
	}

	public void testCase_6() {
		// case setup
		Integer AMOUNT_TEST_REQUESTS = 3;
		Integer THRESHOLD = 3;
		Integer TIMEOUT_PERIOD = 2;
		CircuitBreaker circuit_breaker = CircuitBreaker.CLOSED;
		Integer consecutive_errors = 0;
		Integer test_request_to_go = 3;
		Integer time = 0;
		Integer timestamp_cb_trips = 0;
		// case execution
		Wrapper app = new Wrapper();
		app.initialisation(AMOUNT_TEST_REQUESTS, THRESHOLD, TIMEOUT_PERIOD, circuit_breaker, consecutive_errors,
				test_request_to_go, time, timestamp_cb_trips);
		assertTrue(app.isValid(circuit_breaker, consecutive_errors, test_request_to_go, time, timestamp_cb_trips));
		Boolean microservice_response = false;
		app.request(microservice_response);
		consecutive_errors = 1;
		assertTrue(app.isValid(circuit_breaker, consecutive_errors, test_request_to_go, time, timestamp_cb_trips));
		microservice_response = false;
		app.request(microservice_response);
		consecutive_errors = 2;
		assertTrue(app.isValid(circuit_breaker, consecutive_errors, test_request_to_go, time, timestamp_cb_trips));
		microservice_response = false;
		app.request(microservice_response);
		circuit_breaker = CircuitBreaker.OPEN;
		consecutive_errors = 3;
		assertTrue(app.isValid(circuit_breaker, consecutive_errors, test_request_to_go, time, timestamp_cb_trips));
		app.clock();
		time = 1;
		assertTrue(app.isValid(circuit_breaker, consecutive_errors, test_request_to_go, time, timestamp_cb_trips));
	}

	public void testCase_7() {
		// case setup
		Integer AMOUNT_TEST_REQUESTS = 3;
		Integer THRESHOLD = 3;
		Integer TIMEOUT_PERIOD = 2;
		CircuitBreaker circuit_breaker = CircuitBreaker.CLOSED;
		Integer consecutive_errors = 0;
		Integer test_request_to_go = 3;
		Integer time = 0;
		Integer timestamp_cb_trips = 0;
		// case execution
		Wrapper app = new Wrapper();
		app.initialisation(AMOUNT_TEST_REQUESTS, THRESHOLD, TIMEOUT_PERIOD, circuit_breaker, consecutive_errors,
				test_request_to_go, time, timestamp_cb_trips);
		assertTrue(app.isValid(circuit_breaker, consecutive_errors, test_request_to_go, time, timestamp_cb_trips));
		app.clock();
		time = 1;
		assertTrue(app.isValid(circuit_breaker, consecutive_errors, test_request_to_go, time, timestamp_cb_trips));
		Boolean microservice_response = true;
		app.request(microservice_response);
		assertTrue(app.isValid(circuit_breaker, consecutive_errors, test_request_to_go, time, timestamp_cb_trips));
		microservice_response = true;
		app.request(microservice_response);
		assertTrue(app.isValid(circuit_breaker, consecutive_errors, test_request_to_go, time, timestamp_cb_trips));
		microservice_response = false;
		app.request(microservice_response);
		consecutive_errors = 1;
		assertTrue(app.isValid(circuit_breaker, consecutive_errors, test_request_to_go, time, timestamp_cb_trips));
		microservice_response = false;
		app.request(microservice_response);
		consecutive_errors = 2;
		assertTrue(app.isValid(circuit_breaker, consecutive_errors, test_request_to_go, time, timestamp_cb_trips));
		app.clock();
		time = 2;
		assertTrue(app.isValid(circuit_breaker, consecutive_errors, test_request_to_go, time, timestamp_cb_trips));
		microservice_response = true;
		app.request(microservice_response);
		consecutive_errors = 0;
		assertTrue(app.isValid(circuit_breaker, consecutive_errors, test_request_to_go, time, timestamp_cb_trips));
		app.clock();
		time = 3;
		assertTrue(app.isValid(circuit_breaker, consecutive_errors, test_request_to_go, time, timestamp_cb_trips));
		microservice_response = true;
		app.request(microservice_response);
		assertTrue(app.isValid(circuit_breaker, consecutive_errors, test_request_to_go, time, timestamp_cb_trips));
		app.clock();
		time = 4;
		assertTrue(app.isValid(circuit_breaker, consecutive_errors, test_request_to_go, time, timestamp_cb_trips));
		microservice_response = false;
		app.request(microservice_response);
		consecutive_errors = 1;
		assertTrue(app.isValid(circuit_breaker, consecutive_errors, test_request_to_go, time, timestamp_cb_trips));
		app.clock();
		time = 5;
		assertTrue(app.isValid(circuit_breaker, consecutive_errors, test_request_to_go, time, timestamp_cb_trips));
		microservice_response = true;
		app.request(microservice_response);
		consecutive_errors = 0;
		assertTrue(app.isValid(circuit_breaker, consecutive_errors, test_request_to_go, time, timestamp_cb_trips));
		microservice_response = false;
		app.request(microservice_response);
		consecutive_errors = 1;
		assertTrue(app.isValid(circuit_breaker, consecutive_errors, test_request_to_go, time, timestamp_cb_trips));
		microservice_response = false;
		app.request(microservice_response);
		consecutive_errors = 2;
		assertTrue(app.isValid(circuit_breaker, consecutive_errors, test_request_to_go, time, timestamp_cb_trips));
		microservice_response = false;
		app.request(microservice_response);
		circuit_breaker = CircuitBreaker.OPEN;
		consecutive_errors = 3;
		timestamp_cb_trips = 5;
		assertTrue(app.isValid(circuit_breaker, consecutive_errors, test_request_to_go, time, timestamp_cb_trips));
		app.clock();
		time = 6;
		assertTrue(app.isValid(circuit_breaker, consecutive_errors, test_request_to_go, time, timestamp_cb_trips));
		microservice_response = true;
		app.request(microservice_response);
		consecutive_errors = 0;
		assertTrue(app.isValid(circuit_breaker, consecutive_errors, test_request_to_go, time, timestamp_cb_trips));
		app.clock();
		circuit_breaker = CircuitBreaker.HALF_OPEN;
		time = 7;
		assertTrue(app.isValid(circuit_breaker, consecutive_errors, test_request_to_go, time, timestamp_cb_trips));
		app.clock();
		time = 8;
		assertTrue(app.isValid(circuit_breaker, consecutive_errors, test_request_to_go, time, timestamp_cb_trips));
		microservice_response = true;
		app.request(microservice_response);
		test_request_to_go = 2;
		timestamp_cb_trips = 8;
		assertTrue(app.isValid(circuit_breaker, consecutive_errors, test_request_to_go, time, timestamp_cb_trips));
		microservice_response = true;
		app.request(microservice_response);
		test_request_to_go = 1;
		assertTrue(app.isValid(circuit_breaker, consecutive_errors, test_request_to_go, time, timestamp_cb_trips));
		microservice_response = false;
		app.request(microservice_response);
		circuit_breaker = CircuitBreaker.OPEN;
		test_request_to_go = 3;
		assertTrue(app.isValid(circuit_breaker, consecutive_errors, test_request_to_go, time, timestamp_cb_trips));
		microservice_response = false;
		app.request(microservice_response);
		assertTrue(app.isValid(circuit_breaker, consecutive_errors, test_request_to_go, time, timestamp_cb_trips));
		microservice_response = false;
		app.request(microservice_response);
		assertTrue(app.isValid(circuit_breaker, consecutive_errors, test_request_to_go, time, timestamp_cb_trips));
		app.clock();
		time = 9;
		assertTrue(app.isValid(circuit_breaker, consecutive_errors, test_request_to_go, time, timestamp_cb_trips));
		microservice_response = false;
		app.request(microservice_response);
		assertTrue(app.isValid(circuit_breaker, consecutive_errors, test_request_to_go, time, timestamp_cb_trips));
		microservice_response = true;
		app.request(microservice_response);
		assertTrue(app.isValid(circuit_breaker, consecutive_errors, test_request_to_go, time, timestamp_cb_trips));
		microservice_response = true;
		app.request(microservice_response);
		assertTrue(app.isValid(circuit_breaker, consecutive_errors, test_request_to_go, time, timestamp_cb_trips));
		app.clock();
		circuit_breaker = CircuitBreaker.HALF_OPEN;
		time = 10;
		assertTrue(app.isValid(circuit_breaker, consecutive_errors, test_request_to_go, time, timestamp_cb_trips));
	}

	public void testCase_8() {
		// case setup
		Integer AMOUNT_TEST_REQUESTS = 3;
		Integer THRESHOLD = 3;
		Integer TIMEOUT_PERIOD = 2;
		CircuitBreaker circuit_breaker = CircuitBreaker.CLOSED;
		Integer consecutive_errors = 0;
		Integer test_request_to_go = 3;
		Integer time = 0;
		Integer timestamp_cb_trips = 0;
		// case execution
		Wrapper app = new Wrapper();
		app.initialisation(AMOUNT_TEST_REQUESTS, THRESHOLD, TIMEOUT_PERIOD, circuit_breaker, consecutive_errors,
				test_request_to_go, time, timestamp_cb_trips);
		assertTrue(app.isValid(circuit_breaker, consecutive_errors, test_request_to_go, time, timestamp_cb_trips));
		app.clock();
		time = 1;
		assertTrue(app.isValid(circuit_breaker, consecutive_errors, test_request_to_go, time, timestamp_cb_trips));
		app.clock();
		time = 2;
		assertTrue(app.isValid(circuit_breaker, consecutive_errors, test_request_to_go, time, timestamp_cb_trips));
		app.clock();
		time = 3;
		assertTrue(app.isValid(circuit_breaker, consecutive_errors, test_request_to_go, time, timestamp_cb_trips));
		Boolean microservice_response = false;
		app.request(microservice_response);
		consecutive_errors = 1;
		assertTrue(app.isValid(circuit_breaker, consecutive_errors, test_request_to_go, time, timestamp_cb_trips));
		app.clock();
		time = 4;
		assertTrue(app.isValid(circuit_breaker, consecutive_errors, test_request_to_go, time, timestamp_cb_trips));
		app.clock();
		time = 5;
		assertTrue(app.isValid(circuit_breaker, consecutive_errors, test_request_to_go, time, timestamp_cb_trips));
		microservice_response = true;
		app.request(microservice_response);
		consecutive_errors = 0;
		assertTrue(app.isValid(circuit_breaker, consecutive_errors, test_request_to_go, time, timestamp_cb_trips));
		microservice_response = false;
		app.request(microservice_response);
		consecutive_errors = 1;
		assertTrue(app.isValid(circuit_breaker, consecutive_errors, test_request_to_go, time, timestamp_cb_trips));
		microservice_response = false;
		app.request(microservice_response);
		consecutive_errors = 2;
		assertTrue(app.isValid(circuit_breaker, consecutive_errors, test_request_to_go, time, timestamp_cb_trips));
		microservice_response = true;
		app.request(microservice_response);
		consecutive_errors = 0;
		assertTrue(app.isValid(circuit_breaker, consecutive_errors, test_request_to_go, time, timestamp_cb_trips));
		microservice_response = true;
		app.request(microservice_response);
		assertTrue(app.isValid(circuit_breaker, consecutive_errors, test_request_to_go, time, timestamp_cb_trips));
		microservice_response = true;
		app.request(microservice_response);
		assertTrue(app.isValid(circuit_breaker, consecutive_errors, test_request_to_go, time, timestamp_cb_trips));
		app.clock();
		time = 6;
		assertTrue(app.isValid(circuit_breaker, consecutive_errors, test_request_to_go, time, timestamp_cb_trips));
		microservice_response = true;
		app.request(microservice_response);
		assertTrue(app.isValid(circuit_breaker, consecutive_errors, test_request_to_go, time, timestamp_cb_trips));
		app.clock();
		time = 7;
		assertTrue(app.isValid(circuit_breaker, consecutive_errors, test_request_to_go, time, timestamp_cb_trips));
		microservice_response = true;
		app.request(microservice_response);
		assertTrue(app.isValid(circuit_breaker, consecutive_errors, test_request_to_go, time, timestamp_cb_trips));
		microservice_response = false;
		app.request(microservice_response);
		consecutive_errors = 1;
		assertTrue(app.isValid(circuit_breaker, consecutive_errors, test_request_to_go, time, timestamp_cb_trips));
		microservice_response = false;
		app.request(microservice_response);
		consecutive_errors = 2;
		assertTrue(app.isValid(circuit_breaker, consecutive_errors, test_request_to_go, time, timestamp_cb_trips));
		app.clock();
		time = 8;
		assertTrue(app.isValid(circuit_breaker, consecutive_errors, test_request_to_go, time, timestamp_cb_trips));
		microservice_response = false;
		app.request(microservice_response);
		circuit_breaker = CircuitBreaker.OPEN;
		consecutive_errors = 3;
		timestamp_cb_trips = 8;
		assertTrue(app.isValid(circuit_breaker, consecutive_errors, test_request_to_go, time, timestamp_cb_trips));
		microservice_response = false;
		app.request(microservice_response);
		consecutive_errors = 0;
		assertTrue(app.isValid(circuit_breaker, consecutive_errors, test_request_to_go, time, timestamp_cb_trips));
		app.clock();
		time = 9;
		assertTrue(app.isValid(circuit_breaker, consecutive_errors, test_request_to_go, time, timestamp_cb_trips));
		microservice_response = false;
		app.request(microservice_response);
		assertTrue(app.isValid(circuit_breaker, consecutive_errors, test_request_to_go, time, timestamp_cb_trips));
		microservice_response = false;
		app.request(microservice_response);
		assertTrue(app.isValid(circuit_breaker, consecutive_errors, test_request_to_go, time, timestamp_cb_trips));
		app.clock();
		circuit_breaker = CircuitBreaker.HALF_OPEN;
		time = 10;
		assertTrue(app.isValid(circuit_breaker, consecutive_errors, test_request_to_go, time, timestamp_cb_trips));
		microservice_response = true;
		app.request(microservice_response);
		test_request_to_go = 2;
		timestamp_cb_trips = 10;
		assertTrue(app.isValid(circuit_breaker, consecutive_errors, test_request_to_go, time, timestamp_cb_trips));
		microservice_response = false;
		app.request(microservice_response);
		circuit_breaker = CircuitBreaker.OPEN;
		test_request_to_go = 3;
		assertTrue(app.isValid(circuit_breaker, consecutive_errors, test_request_to_go, time, timestamp_cb_trips));
		app.clock();
		time = 11;
		assertTrue(app.isValid(circuit_breaker, consecutive_errors, test_request_to_go, time, timestamp_cb_trips));
		app.clock();
		circuit_breaker = CircuitBreaker.HALF_OPEN;
		time = 12;
		assertTrue(app.isValid(circuit_breaker, consecutive_errors, test_request_to_go, time, timestamp_cb_trips));
		microservice_response = false;
		app.request(microservice_response);
		circuit_breaker = CircuitBreaker.OPEN;
		timestamp_cb_trips = 12;
		assertTrue(app.isValid(circuit_breaker, consecutive_errors, test_request_to_go, time, timestamp_cb_trips));
	}

	public void testCase_9() {
		// case setup
		Integer AMOUNT_TEST_REQUESTS = 3;
		Integer THRESHOLD = 3;
		Integer TIMEOUT_PERIOD = 2;
		CircuitBreaker circuit_breaker = CircuitBreaker.CLOSED;
		Integer consecutive_errors = 0;
		Integer test_request_to_go = 3;
		Integer time = 0;
		Integer timestamp_cb_trips = 0;
		// case execution
		Wrapper app = new Wrapper();
		app.initialisation(AMOUNT_TEST_REQUESTS, THRESHOLD, TIMEOUT_PERIOD, circuit_breaker, consecutive_errors,
				test_request_to_go, time, timestamp_cb_trips);
		assertTrue(app.isValid(circuit_breaker, consecutive_errors, test_request_to_go, time, timestamp_cb_trips));
		Boolean microservice_response = true;
		app.request(microservice_response);
		assertTrue(app.isValid(circuit_breaker, consecutive_errors, test_request_to_go, time, timestamp_cb_trips));
		microservice_response = false;
		app.request(microservice_response);
		consecutive_errors = 1;
		assertTrue(app.isValid(circuit_breaker, consecutive_errors, test_request_to_go, time, timestamp_cb_trips));
		microservice_response = true;
		app.request(microservice_response);
		consecutive_errors = 0;
		assertTrue(app.isValid(circuit_breaker, consecutive_errors, test_request_to_go, time, timestamp_cb_trips));
		microservice_response = false;
		app.request(microservice_response);
		consecutive_errors = 1;
		assertTrue(app.isValid(circuit_breaker, consecutive_errors, test_request_to_go, time, timestamp_cb_trips));
		microservice_response = false;
		app.request(microservice_response);
		consecutive_errors = 2;
		assertTrue(app.isValid(circuit_breaker, consecutive_errors, test_request_to_go, time, timestamp_cb_trips));
		microservice_response = true;
		app.request(microservice_response);
		consecutive_errors = 0;
		assertTrue(app.isValid(circuit_breaker, consecutive_errors, test_request_to_go, time, timestamp_cb_trips));
		microservice_response = true;
		app.request(microservice_response);
		assertTrue(app.isValid(circuit_breaker, consecutive_errors, test_request_to_go, time, timestamp_cb_trips));
		microservice_response = false;
		app.request(microservice_response);
		consecutive_errors = 1;
		assertTrue(app.isValid(circuit_breaker, consecutive_errors, test_request_to_go, time, timestamp_cb_trips));
		app.clock();
		time = 1;
		assertTrue(app.isValid(circuit_breaker, consecutive_errors, test_request_to_go, time, timestamp_cb_trips));
		microservice_response = false;
		app.request(microservice_response);
		consecutive_errors = 2;
		assertTrue(app.isValid(circuit_breaker, consecutive_errors, test_request_to_go, time, timestamp_cb_trips));
		microservice_response = false;
		app.request(microservice_response);
		circuit_breaker = CircuitBreaker.OPEN;
		consecutive_errors = 3;
		timestamp_cb_trips = 1;
		assertTrue(app.isValid(circuit_breaker, consecutive_errors, test_request_to_go, time, timestamp_cb_trips));
		microservice_response = false;
		app.request(microservice_response);
		consecutive_errors = 0;
		assertTrue(app.isValid(circuit_breaker, consecutive_errors, test_request_to_go, time, timestamp_cb_trips));
		microservice_response = true;
		app.request(microservice_response);
		assertTrue(app.isValid(circuit_breaker, consecutive_errors, test_request_to_go, time, timestamp_cb_trips));
		microservice_response = false;
		app.request(microservice_response);
		assertTrue(app.isValid(circuit_breaker, consecutive_errors, test_request_to_go, time, timestamp_cb_trips));
		microservice_response = false;
		app.request(microservice_response);
		assertTrue(app.isValid(circuit_breaker, consecutive_errors, test_request_to_go, time, timestamp_cb_trips));
		microservice_response = false;
		app.request(microservice_response);
		assertTrue(app.isValid(circuit_breaker, consecutive_errors, test_request_to_go, time, timestamp_cb_trips));
		microservice_response = false;
		app.request(microservice_response);
		assertTrue(app.isValid(circuit_breaker, consecutive_errors, test_request_to_go, time, timestamp_cb_trips));
		app.clock();
		time = 2;
		assertTrue(app.isValid(circuit_breaker, consecutive_errors, test_request_to_go, time, timestamp_cb_trips));
		app.clock();
		circuit_breaker = CircuitBreaker.HALF_OPEN;
		time = 3;
		assertTrue(app.isValid(circuit_breaker, consecutive_errors, test_request_to_go, time, timestamp_cb_trips));
		microservice_response = true;
		app.request(microservice_response);
		test_request_to_go = 2;
		timestamp_cb_trips = 3;
		assertTrue(app.isValid(circuit_breaker, consecutive_errors, test_request_to_go, time, timestamp_cb_trips));
		microservice_response = false;
		app.request(microservice_response);
		circuit_breaker = CircuitBreaker.OPEN;
		test_request_to_go = 3;
		assertTrue(app.isValid(circuit_breaker, consecutive_errors, test_request_to_go, time, timestamp_cb_trips));
		app.clock();
		time = 4;
		assertTrue(app.isValid(circuit_breaker, consecutive_errors, test_request_to_go, time, timestamp_cb_trips));
		microservice_response = false;
		app.request(microservice_response);
		assertTrue(app.isValid(circuit_breaker, consecutive_errors, test_request_to_go, time, timestamp_cb_trips));
		microservice_response = true;
		app.request(microservice_response);
		assertTrue(app.isValid(circuit_breaker, consecutive_errors, test_request_to_go, time, timestamp_cb_trips));
		app.clock();
		circuit_breaker = CircuitBreaker.HALF_OPEN;
		time = 5;
		assertTrue(app.isValid(circuit_breaker, consecutive_errors, test_request_to_go, time, timestamp_cb_trips));
		microservice_response = false;
		app.request(microservice_response);
		circuit_breaker = CircuitBreaker.OPEN;
		timestamp_cb_trips = 5;
		assertTrue(app.isValid(circuit_breaker, consecutive_errors, test_request_to_go, time, timestamp_cb_trips));
		microservice_response = true;
		app.request(microservice_response);
		assertTrue(app.isValid(circuit_breaker, consecutive_errors, test_request_to_go, time, timestamp_cb_trips));
		microservice_response = false;
		app.request(microservice_response);
		assertTrue(app.isValid(circuit_breaker, consecutive_errors, test_request_to_go, time, timestamp_cb_trips));
		microservice_response = true;
		app.request(microservice_response);
		assertTrue(app.isValid(circuit_breaker, consecutive_errors, test_request_to_go, time, timestamp_cb_trips));
		microservice_response = true;
		app.request(microservice_response);
		assertTrue(app.isValid(circuit_breaker, consecutive_errors, test_request_to_go, time, timestamp_cb_trips));
	}

}