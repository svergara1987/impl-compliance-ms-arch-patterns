package uy.edu.fing.svergara;

import java.io.IOException;

import com.google.gson.Gson;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Hello world!
 *
 */
public class Wrapper {

	/**
	 * Initialization runs once before the first test in test suite
	 */
	public void initialisation(Integer AMOUNT_TEST_REQUESTS, Integer THRESHOLD, Integer TIMEOUT_PERIOD,
			CircuitBreaker circuit_breaker, Integer consecutive_errors, Integer test_request_to_go, Integer time,
			Integer timestamp_cb_trips) {
		System.out.println("[test log] [" + System.currentTimeMillis() + "] initialisation()");
		request(true);
		String requestURL = "http://localhost:8090/reset";
		OkHttpClient client = new OkHttpClient().newBuilder().build();
		Request request = new Request.Builder().url(requestURL).method("DELETE", null).build();
		try {
			Response response = client.newCall(request).execute();
			System.out.println(
					"[test log] [" + System.currentTimeMillis() + "] initialization status = " + response.code());
		} catch (IOException e) {
			throw new RuntimeException("exception when calling rest api to reset circuit breaker", e);
		}
	}

	public void request(Boolean microservice_response) {
		String requestURL = "http://localhost:8090/people";
		if (!microservice_response) {
			requestURL += "?fail=true";
		}
		OkHttpClient client = new OkHttpClient().newBuilder().build();
		Request request = new Request.Builder().url(requestURL).method("GET", null).build();
		try {
			Response response = client.newCall(request).execute();
			System.out.println("[test log] [" + System.currentTimeMillis() + "] request(microservice_response="
					+ microservice_response + ") = " + response.body().string());
		} catch (IOException e) {
			throw new RuntimeException("exception when calling rest api", e);
		}
	}

	public void clock() {
		System.out.println("[test log] [" + System.currentTimeMillis() + "] starting clock()");
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			throw new RuntimeException("exception while trying to sleep", e);
		}
		System.out.println("[test log] [" + System.currentTimeMillis() + "] ended clock()");
	}

	/**
	 * @return true only if
	 * 
	 *         - circuit_breaker equals to @param circuit_breaker
	 * 
	 *         - time equals to @param time
	 * 
	 *         - test_request_to_go equals to @param test_request_to_go
	 * 
	 *         - timestamp_cb_trips equals to @param timestamp_cb_trips
	 * 
	 *         - consecutive_errors equals to @param consecutive_errors
	 */
	public boolean isValid(CircuitBreaker circuit_breaker, Integer consecutive_errors, Integer test_request_to_go,
			Integer time, Integer timestamp_cb_trips) {
		System.out.println(
				"[test log] [" + System.currentTimeMillis() + "] isValid_circuit_breaker(" + circuit_breaker + ")");
		String requestURL = "http://localhost:8090/status";
		OkHttpClient client = new OkHttpClient().newBuilder().build();
		Request request = new Request.Builder().url(requestURL).method("GET", null).build();
		try {
			Response response = client.newCall(request).execute();
			String strResponse = response.body().string();
			System.out.println("[test log] [" + System.currentTimeMillis() + "] test request = " + strResponse);
			Status status = new Gson().fromJson(strResponse, Status.class);
			if (status == null) {
				// hystrix works with commands (per operation), if stauts == null then circuit
				// breaker operation was never invoked, so circuit breaker status is the same as
				// when initialized
				return true;
			} else {
				if (circuit_breaker.equals(CircuitBreaker.CLOSED)) {
					return !status.isOpen();
				} else if (circuit_breaker.equals(CircuitBreaker.OPEN)) {
					return status.isOpen();
				} else {
					// TODO how to validate HALF-OPEN state
					return true;
				}
			}
		} catch (IOException e) {
			throw new RuntimeException("exception when calling rest api to validate circuit breaker", e);
		}
	}

}
