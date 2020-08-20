package ${packageName};

public class Wrapper {
	
<#list operations as anOperation>
	public void ${anOperation.name} (${anOperation.parametersStrRep?join(", ")}) {
		// TODO generated code you should complete
	}
		
</#list>

	
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
	public boolean isValid(circuit_breaker circuit_breaker, Integer time, Integer test_request_to_go,
			Integer timestamp_cb_trips, Integer consecutive_errors) {
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
				if (circuit_breaker.equals(uy.edu.fing.svergara.circuit_breaker.CLOSED)) {
					return !status.isOpen();
				} else if (circuit_breaker.equals(uy.edu.fing.svergara.circuit_breaker.OPEN)) {
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
