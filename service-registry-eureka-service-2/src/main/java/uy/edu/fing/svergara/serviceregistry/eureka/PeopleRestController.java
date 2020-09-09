package uy.edu.fing.svergara.serviceregistry.eureka;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
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

import uy.edu.fing.svergara.serviceregistry.eureka.model.Person;
import uy.edu.fing.svergara.serviceregistry.eureka.service.PeopleService;

@RestController
public class PeopleRestController {

	@Autowired
	PeopleService peopleService;
	@Autowired
	RestTemplate restTemplate;
	@Autowired
	private DiscoveryClient discoveryClient;

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

	@SuppressWarnings("unused")
	private ResponseEntity<Object> fallbackList(Integer milliseconds, Boolean fail) {
		return new ResponseEntity<>("fallback list", HttpStatus.OK);
	}

	@Bean
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}

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

//	@RequestMapping(method = RequestMethod.DELETE, value = "/reset")
//	public ResponseEntity<Object> reset() {
//		Hystrix.reset();
//		return new ResponseEntity<>(HttpStatus.OK);
//	}

	@RequestMapping(method = RequestMethod.GET, value = "/people/{email}")
	public ResponseEntity<Object> retrieve(@PathVariable("email") String email) {
		return new ResponseEntity<>(peopleService.retrieve(email), HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/status")
	public ResponseEntity<Object> status() {
		List<ServiceInstance> list = discoveryClient.getInstances("SERVICE-REGISTRY-EUREKA-CLIENT-PEOPLE");
		return new ResponseEntity<>(list, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/people/{email}")
	public ResponseEntity<Object> update(@PathVariable("email") String currentEmail, @RequestBody Person person) {
		return new ResponseEntity<>(peopleService.update(currentEmail, person), HttpStatus.OK);
	}
}
