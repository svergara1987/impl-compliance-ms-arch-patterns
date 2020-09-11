package uy.edu.fing.svergara;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Wrapper {

	public void initialisation(Integer AMOUNT_TEST_REQUESTS, Integer THRESHOLD, Integer TIMEOUT_PERIOD,
			CircuitBreaker circuit_breaker, Integer consecutive_errors, Integer test_request_to_go, Integer time,
			Integer timestamp_cb_trips) {
		System.out.println(
				"---------------------------------------------------------------------------------------------------");
		request(true);
		String requestURL = "http://localhost:8091/reset";
		OkHttpClient client = new OkHttpClient().newBuilder().build();
		Request request = new Request.Builder().url(requestURL).method("DELETE", null).build();
		try {
			System.out.println("initialization " + client.newCall(request).execute().code());
		} catch (IOException e) {
			throw new RuntimeException("exception when calling rest api to reset circuit breaker", e);
		}
	}

	public Boolean isValid(CircuitBreaker circuit_breaker, Integer consecutive_errors, Integer test_request_to_go,
			Integer time, Integer timestamp_cb_trips) {
		String strResponse = status();
		boolean equals = false;
		if (circuit_breaker.equals(CircuitBreaker.CLOSED)) {
			equals = "CLOSED".equalsIgnoreCase(strResponse);
		} else if (circuit_breaker.equals(CircuitBreaker.OPEN)) {
			equals = "OPEN".equalsIgnoreCase(strResponse);
		} else {
			equals = "HALF_OPEN".equalsIgnoreCase(strResponse);
		}
		System.out.println("isValid " + circuit_breaker + " = " + strResponse + " --> " + equals);
		return equals;
	}

	private String status() {
		String requestURL = "http://localhost:8091/status";
		OkHttpClient client = new OkHttpClient().newBuilder().build();
		Request request = new Request.Builder().url(requestURL).method("GET", null).build();
		try {
			Response response = client.newCall(request).execute();
			String status = response.body().string().replaceAll("\"", "");
			System.out.println("status = " + status);
			return status;
		} catch (IOException e) {
			throw new RuntimeException("exception when calling rest api to validate circuit breaker", e);
		}
	}

	public void request(Boolean microservice_response) {
		String requestURL = "http://localhost:8091/people";
		if (!microservice_response) {
			requestURL += "?fail=true";
		}
		OkHttpClient client = new OkHttpClient().newBuilder().build();
		Request request = new Request.Builder().url(requestURL).method("GET", null).build();
		try {
			Response response = client.newCall(request).execute();
			System.out.println(
					"request(microservice_response=" + microservice_response + ") = " + response.body().string());
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

}
