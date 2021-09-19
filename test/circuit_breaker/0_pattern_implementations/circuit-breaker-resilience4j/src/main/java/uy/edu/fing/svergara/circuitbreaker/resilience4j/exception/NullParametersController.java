package uy.edu.fing.svergara.circuitbreaker.resilience4j.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class NullParametersController {

	@ExceptionHandler(value = NullParametersException.class)
	public ResponseEntity<Object> exception(NullParametersException exception) {
		return new ResponseEntity<>(exception.getParameters() + " must have a value", HttpStatus.BAD_REQUEST);
	}

}
