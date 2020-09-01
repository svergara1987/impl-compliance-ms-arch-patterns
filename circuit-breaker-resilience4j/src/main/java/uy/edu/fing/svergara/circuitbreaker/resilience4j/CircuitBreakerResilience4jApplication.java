package uy.edu.fing.svergara.circuitbreaker.resilience4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.cloud.client.circuitbreaker.Customizer;
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

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreaker.State;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig.SlidingWindowType;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import uy.edu.fing.svergara.circuitbreaker.resilience4j.exception.ErrorGettingStatusException;
import uy.edu.fing.svergara.circuitbreaker.resilience4j.exception.ErrorResettingCiurcuitBreakerException;
import uy.edu.fing.svergara.circuitbreaker.resilience4j.model.Person;
import uy.edu.fing.svergara.circuitbreaker.resilience4j.service.PeopleService;

@RestController
@SpringBootApplication
public class CircuitBreakerResilience4jApplication {

	private static final int AMOUNT_TEST_REQUESTS = 3;
	private static final String CIRCUIT_BREAKER_LIST_PEOPLE = "list-people";
	private static final long EXECUTION_TIMEOUT_IN_MILLISECONDS = 3000;
	private static final float FAILURE_RATE_THRESHOLD = 100;
	private static final int MINIMUM_NUMBER_OF_CALLS_TO_EVALUATE_TRIPPING = 3;
	private static final int SLIDING_WINDOW_SIZE = 3;
	private static final long TIMEOUT_PERIOD_IN_MILLISECONDS = 2000;

	public static void main(String[] args) {
		SpringApplication.run(CircuitBreakerResilience4jApplication.class, args);
	}

	@Autowired
	CircuitBreakerFactory<?, ?> circuitBreakerFactory;
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

	@Bean
	public Customizer<Resilience4JCircuitBreakerFactory> defaultCustomizer() {
		return factory -> factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
				.timeLimiterConfig(TimeLimiterConfig.custom()
						.timeoutDuration(Duration.ofMillis(EXECUTION_TIMEOUT_IN_MILLISECONDS)).build())
				.circuitBreakerConfig(CircuitBreakerConfig.custom()
						.waitDurationInOpenState(Duration.ofMillis(TIMEOUT_PERIOD_IN_MILLISECONDS))
						.enableAutomaticTransitionFromOpenToHalfOpen()
						.slidingWindow(SLIDING_WINDOW_SIZE, MINIMUM_NUMBER_OF_CALLS_TO_EVALUATE_TRIPPING,
								SlidingWindowType.COUNT_BASED)
						.failureRateThreshold(FAILURE_RATE_THRESHOLD)
						.permittedNumberOfCallsInHalfOpenState(AMOUNT_TEST_REQUESTS).build())
				.build());
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/people/{email}")
	public ResponseEntity<Object> delete(@PathVariable("email") String currentEmail) {
		return new ResponseEntity<>(peopleService.delete(currentEmail), HttpStatus.OK);
	}

	private ResponseEntity<Object> fallbackList(Integer milliseconds, Boolean fail) {
		System.out.println("fallback - circuit breaker state is " + circuitBreakerState(CIRCUIT_BREAKER_LIST_PEOPLE));
		return new ResponseEntity<>("fallback list", HttpStatus.OK);
	}

	@Bean
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/people")
	public ResponseEntity<Object> list(@RequestParam(value = "delayed", required = false) Integer milliseconds,
			@RequestParam(value = "fail", required = false) Boolean fail) {
		System.out.println("list - fail " + fail);
		return circuitBreakerFactory.create(CIRCUIT_BREAKER_LIST_PEOPLE)
				.run(peopleService.listSuppplier(milliseconds, fail), t -> fallbackList(milliseconds, fail));
	}

	@RequestMapping(method = RequestMethod.POST, value = "/people")
	public ResponseEntity<Object> register(@RequestBody Person person) {
		return new ResponseEntity<>(peopleService.register(person), HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/reset")
	public ResponseEntity<Object> reset() {
		try {
			Method getCircuitBreakerRegistryMethod = circuitBreakerFactory.getClass()
					.getDeclaredMethod("getCircuitBreakerRegistry");
			getCircuitBreakerRegistryMethod.setAccessible(true);
			CircuitBreakerRegistry circuitBreakerRegistry = (CircuitBreakerRegistry) getCircuitBreakerRegistryMethod
					.invoke(circuitBreakerFactory);
			CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker(CIRCUIT_BREAKER_LIST_PEOPLE);
			circuitBreaker.reset();
			System.out.println("reset - circuit breaker state is " + circuitBreakerState(CIRCUIT_BREAKER_LIST_PEOPLE));
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			throw new ErrorResettingCiurcuitBreakerException(CIRCUIT_BREAKER_LIST_PEOPLE, e);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/people/{email}")
	public ResponseEntity<Object> retrieve(@PathVariable("email") String email) {
		return new ResponseEntity<>(peopleService.retrieve(email), HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/status")
	public ResponseEntity<Object> status() {
		State state = circuitBreakerState(CIRCUIT_BREAKER_LIST_PEOPLE);
		System.out.println("status - circuit breaker state is " + state);
		return new ResponseEntity<>(state, HttpStatus.OK);
	}

	private State circuitBreakerState(String circuitBreaker) {
		try {
			Method getCircuitBreakerRegistryMethod = circuitBreakerFactory.getClass()
					.getDeclaredMethod("getCircuitBreakerRegistry");
			getCircuitBreakerRegistryMethod.setAccessible(true);
			CircuitBreakerRegistry circuitBreakerRegistry = (CircuitBreakerRegistry) getCircuitBreakerRegistryMethod
					.invoke(circuitBreakerFactory);
			return circuitBreakerRegistry.circuitBreaker(circuitBreaker).getState();
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			throw new ErrorGettingStatusException(CIRCUIT_BREAKER_LIST_PEOPLE, e);
		}
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/people/{email}")
	public ResponseEntity<Object> update(@PathVariable("email") String currentEmail, @RequestBody Person person) {
		return new ResponseEntity<>(peopleService.update(currentEmail, person), HttpStatus.OK);
	}
}
