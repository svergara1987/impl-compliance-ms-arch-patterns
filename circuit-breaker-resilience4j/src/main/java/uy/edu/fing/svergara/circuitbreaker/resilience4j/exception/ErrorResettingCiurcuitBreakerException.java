package uy.edu.fing.svergara.circuitbreaker.resilience4j.exception;

public class ErrorResettingCiurcuitBreakerException extends RuntimeException {

	private static final long serialVersionUID = -1777709780041204111L;
	private String circuitBreaker;
	private Throwable throwable;

	public ErrorResettingCiurcuitBreakerException(String circuitBreaker, Throwable throwable) {
		this.throwable = throwable;
		this.circuitBreaker = circuitBreaker;
	}

	public String getCircuitBreaker() {
		return circuitBreaker;
	}

	public Throwable getThrowable() {
		return throwable;
	}

}
