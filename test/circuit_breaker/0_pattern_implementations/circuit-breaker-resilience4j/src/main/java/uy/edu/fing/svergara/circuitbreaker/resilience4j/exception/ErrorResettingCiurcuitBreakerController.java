package uy.edu.fing.svergara.circuitbreaker.resilience4j.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ErrorResettingCiurcuitBreakerController {

	@ExceptionHandler(value = ErrorResettingCiurcuitBreakerException.class)
	public ResponseEntity<Object> exception(ErrorResettingCiurcuitBreakerException exception) {
		exception.getThrowable().printStackTrace();
		return new ResponseEntity<>("error resetting " + exception.getCircuitBreaker() + " circuit breaker status",
				HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
