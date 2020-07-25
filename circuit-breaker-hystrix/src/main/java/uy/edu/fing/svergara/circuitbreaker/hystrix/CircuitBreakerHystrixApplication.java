package uy.edu.fing.svergara.circuitbreaker.hystrix;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

import uy.edu.fing.svergara.circuitbreaker.hystrix.model.Person;
import uy.edu.fing.svergara.circuitbreaker.hystrix.service.PeopleService;

@RestController
@EnableHystrix
@SpringBootApplication
public class CircuitBreakerHystrixApplication {

	private static final String EXECUTION_TIMEOUT_IN_MILLISECONDS = "3000"; // This property sets the time in
																			// milliseconds after which the caller will
																			// observe a timeout and walk away from the
																			// command execution. Hystrix marks the
																			// HystrixCommand as a TIMEOUT, and performs
																			// fallback logic. Note that there is
																			// configuration for turning off timeouts
																			// per-command, if that is desired (see
																			// command.timeout.enabled).
	private static final String THRESHOLD = "3"; // This property sets the minimum number of requests in a rolling
													// window that will trip the circuit. For example, if the value is
													// 20, then if only 19 requests are received in the rolling window
													// (say a window of 10 seconds) the circuit will not trip open even
													// if all 19 failed.
	private static final String ERROR_THRESHOLD_PERCENTAGE = "50"; // This property sets the error percentage at or
																	// above which the circuit should trip open and
																	// start short-circuiting requests to fallback
																	// logic.
	private static final String ROLLING_WINDOW_TIME = "10000"; // This property sets the duration of the statistical
																// rolling window, in milliseconds. This is how long
																// metrics are kept for the thread pool.
	private static final String TIMEOUT_PERIOD = "3000"; // This property sets the amount of time, after tripping the
															// circuit, to reject requests before allowing attempts
															// again to determine if the circuit should again be closed.

	public static void main(String[] args) {
		SpringApplication.run(CircuitBreakerHystrixApplication.class, args);
	}

	@Autowired
	PeopleService peopleService;
	@Autowired
	RestTemplate restTemplate;

	@RequestMapping(method = { RequestMethod.PUT, RequestMethod.POST }, value = "/people/{email}/{skill}")
	public ResponseEntity<Object> addOrUpdateSkill(@PathVariable("email") String currentEmail,
			@PathVariable("skill") String skillName,
			@RequestParam(value = "value", required = true) Integer skillValue) {
		return new ResponseEntity<>(peopleService.addOrUpdateSkill(currentEmail, skillName, skillValue), HttpStatus.OK);

	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/people/{email}")
	public ResponseEntity<Object> delete(@PathVariable("email") String currentEmail) {
		return new ResponseEntity<>(peopleService.delete(currentEmail), HttpStatus.OK);
	}

	@Bean
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}

	@HystrixCommand(fallbackMethod = "fallbackList", commandProperties = {
			@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = EXECUTION_TIMEOUT_IN_MILLISECONDS),
			@HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = THRESHOLD),
			@HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = ERROR_THRESHOLD_PERCENTAGE),
			@HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = ROLLING_WINDOW_TIME),
			@HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = TIMEOUT_PERIOD) })
	@RequestMapping(method = RequestMethod.GET, value = "/people")
	public ResponseEntity<Object> list(@RequestParam(value = "delayed", required = false) Integer milliseconds,
			@RequestParam(value = "fail", required = false) Boolean fail) throws InterruptedException {
		if (milliseconds != null) {
			Thread.sleep(milliseconds);
		}
		if (fail != null && fail) {
			throw new RuntimeException("Unhandled failure");
		}
		return new ResponseEntity<>(peopleService.list(), HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/people")
	public ResponseEntity<Object> register(@RequestBody Person person) {
		return new ResponseEntity<>(peopleService.register(person), HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/people/{email}")
	public ResponseEntity<Object> retrieve(@PathVariable("email") String email) {
		return new ResponseEntity<>(peopleService.retrieve(email), HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/people/{email}")
	public ResponseEntity<Object> update(@PathVariable("email") String currentEmail, @RequestBody Person person) {
		return new ResponseEntity<>(peopleService.update(currentEmail, person), HttpStatus.OK);
	}

	@SuppressWarnings("unused")
	private ResponseEntity<Object> fallbackList(Integer milliseconds, Boolean fail) {
		return new ResponseEntity<>("fallback list", HttpStatus.OK);
	}
}
