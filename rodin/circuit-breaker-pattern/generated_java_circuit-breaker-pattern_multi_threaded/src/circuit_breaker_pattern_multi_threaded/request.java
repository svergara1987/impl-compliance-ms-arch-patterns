package circuit_breaker_pattern_multi_threaded; 

import eventb_prelude.*;
import Util.Utilities;

public class request extends Thread{
	/*@ spec_public */ private m0_circuit_breaker machine; // reference to the machine 

	/*@ public normal_behavior
		requires true;
		assignable \everything;
		ensures this.machine == m; */
	public request(m0_circuit_breaker m) {
		this.machine = m;
	}

	/*@ public normal_behavior
		requires true;
 		assignable \nothing;
		ensures \result <==> (BOOL.instance.has(microservice_response) && machine.CIRCUIT_BREAKER_STATE.has(new_circuit_breaker) && NAT.instance.has(new_consecutive_errors) && NAT.instance.has(new_timestamp_cb_trips) && NAT.instance.has(new_test_request_to_go) && ((machine.get_circuit_breaker().equals(machine.CLOSED) && microservice_response.equals(true)) ==> (new_consecutive_errors.equals(new Integer(0)))) && ((machine.get_circuit_breaker().equals(machine.CLOSED) && microservice_response.equals(false)) ==> (new_consecutive_errors.equals(new Integer(machine.get_consecutive_errors() + new Integer(1))))) && ((machine.get_circuit_breaker().equals(machine.CLOSED) && (new_consecutive_errors).compareTo(machine.THRESHOLD) >= 0) ==> (new_circuit_breaker.equals(machine.OPEN) && new_timestamp_cb_trips.equals(machine.get_time()))) && ((machine.get_circuit_breaker().equals(machine.CLOSED) && (new_consecutive_errors).compareTo(machine.THRESHOLD) < 0) ==> (new_circuit_breaker.equals(machine.CLOSED) && new_timestamp_cb_trips.equals(machine.get_timestamp_cb_trips()))) && ((machine.get_circuit_breaker().equals(machine.CLOSED)) ==> (new_test_request_to_go.equals(machine.AMOUNT_TEST_REQUESTS))) && ((machine.get_circuit_breaker().equals(machine.OPEN)) ==> (new_circuit_breaker.equals(machine.OPEN) && new_consecutive_errors.equals(new Integer(0)) && new_test_request_to_go.equals(machine.AMOUNT_TEST_REQUESTS) && new_timestamp_cb_trips.equals(machine.get_timestamp_cb_trips()))) && ((machine.get_circuit_breaker().equals(machine.HALF_OPEN) && microservice_response.equals(false)) ==> (new_circuit_breaker.equals(machine.OPEN) && new_test_request_to_go.equals(machine.AMOUNT_TEST_REQUESTS))) && ((machine.get_circuit_breaker().equals(machine.HALF_OPEN) && microservice_response.equals(true)) ==> (new_test_request_to_go.equals(new Integer(machine.get_test_request_to_go() - new Integer(1))))) && ((machine.get_circuit_breaker().equals(machine.HALF_OPEN) && microservice_response.equals(true) && new_test_request_to_go.equals(new Integer(0))) ==> (new_circuit_breaker.equals(machine.CLOSED))) && ((machine.get_circuit_breaker().equals(machine.HALF_OPEN) && microservice_response.equals(true) && (new_test_request_to_go).compareTo(new Integer(0)) > 0) ==> (new_circuit_breaker.equals(machine.HALF_OPEN))) && ((machine.get_circuit_breaker().equals(machine.HALF_OPEN)) ==> (new_consecutive_errors.equals(new Integer(0)) && new_timestamp_cb_trips.equals(machine.get_time())))); */
	public /*@ pure */ boolean guard_request( Boolean microservice_response, Integer new_circuit_breaker, Integer new_consecutive_errors, Integer new_test_request_to_go, Integer new_timestamp_cb_trips) {
		return (BOOL.instance.has(microservice_response) && machine.CIRCUIT_BREAKER_STATE.has(new_circuit_breaker) && NAT.instance.has(new_consecutive_errors) && NAT.instance.has(new_timestamp_cb_trips) && NAT.instance.has(new_test_request_to_go) && BOOL.implication(machine.get_circuit_breaker().equals(machine.CLOSED) && microservice_response.equals(true),new_consecutive_errors.equals(new Integer(0))) && BOOL.implication(machine.get_circuit_breaker().equals(machine.CLOSED) && microservice_response.equals(false),new_consecutive_errors.equals(new Integer(machine.get_consecutive_errors() + new Integer(1)))) && BOOL.implication(machine.get_circuit_breaker().equals(machine.CLOSED) && (new_consecutive_errors).compareTo(machine.THRESHOLD) >= 0,new_circuit_breaker.equals(machine.OPEN) && new_timestamp_cb_trips.equals(machine.get_time())) && BOOL.implication(machine.get_circuit_breaker().equals(machine.CLOSED) && (new_consecutive_errors).compareTo(machine.THRESHOLD) < 0,new_circuit_breaker.equals(machine.CLOSED) && new_timestamp_cb_trips.equals(machine.get_timestamp_cb_trips())) && BOOL.implication(machine.get_circuit_breaker().equals(machine.CLOSED),new_test_request_to_go.equals(machine.AMOUNT_TEST_REQUESTS)) && BOOL.implication(machine.get_circuit_breaker().equals(machine.OPEN),new_circuit_breaker.equals(machine.OPEN) && new_consecutive_errors.equals(new Integer(0)) && new_test_request_to_go.equals(machine.AMOUNT_TEST_REQUESTS) && new_timestamp_cb_trips.equals(machine.get_timestamp_cb_trips())) && BOOL.implication(machine.get_circuit_breaker().equals(machine.HALF_OPEN) && microservice_response.equals(false),new_circuit_breaker.equals(machine.OPEN) && new_test_request_to_go.equals(machine.AMOUNT_TEST_REQUESTS)) && BOOL.implication(machine.get_circuit_breaker().equals(machine.HALF_OPEN) && microservice_response.equals(true),new_test_request_to_go.equals(new Integer(machine.get_test_request_to_go() - new Integer(1)))) && BOOL.implication(machine.get_circuit_breaker().equals(machine.HALF_OPEN) && microservice_response.equals(true) && new_test_request_to_go.equals(new Integer(0)),new_circuit_breaker.equals(machine.CLOSED)) && BOOL.implication(machine.get_circuit_breaker().equals(machine.HALF_OPEN) && microservice_response.equals(true) && (new_test_request_to_go).compareTo(new Integer(0)) > 0,new_circuit_breaker.equals(machine.HALF_OPEN)) && BOOL.implication(machine.get_circuit_breaker().equals(machine.HALF_OPEN),new_consecutive_errors.equals(new Integer(0)) && new_timestamp_cb_trips.equals(machine.get_time())));
	}

	/*@ public normal_behavior
		requires guard_request(microservice_response,new_circuit_breaker,new_consecutive_errors,new_test_request_to_go,new_timestamp_cb_trips);
		assignable machine.consecutive_errors, machine.circuit_breaker, machine.timestamp_cb_trips, machine.test_request_to_go;
		ensures guard_request(microservice_response,new_circuit_breaker,new_consecutive_errors,new_test_request_to_go,new_timestamp_cb_trips) &&  machine.get_consecutive_errors() == \old(new_consecutive_errors) &&  machine.get_circuit_breaker() == \old(new_circuit_breaker) &&  machine.get_timestamp_cb_trips() == \old(new_timestamp_cb_trips) &&  machine.get_test_request_to_go() == \old(new_test_request_to_go); 
	 also
		requires !guard_request(microservice_response,new_circuit_breaker,new_consecutive_errors,new_test_request_to_go,new_timestamp_cb_trips);
		assignable \nothing;
		ensures true; */
	public void run_request( Boolean microservice_response, Integer new_circuit_breaker, Integer new_consecutive_errors, Integer new_test_request_to_go, Integer new_timestamp_cb_trips){
		if(guard_request(microservice_response,new_circuit_breaker,new_consecutive_errors,new_test_request_to_go,new_timestamp_cb_trips)) {
			Integer consecutive_errors_tmp = machine.get_consecutive_errors();
			Integer circuit_breaker_tmp = machine.get_circuit_breaker();
			Integer timestamp_cb_trips_tmp = machine.get_timestamp_cb_trips();
			Integer test_request_to_go_tmp = machine.get_test_request_to_go();

			machine.set_consecutive_errors(new_consecutive_errors);
			machine.set_circuit_breaker(new_circuit_breaker);
			machine.set_timestamp_cb_trips(new_timestamp_cb_trips);
			machine.set_test_request_to_go(new_test_request_to_go);

			System.out.println("request executed microservice_response: " + microservice_response + " new_circuit_breaker: " + new_circuit_breaker + " new_consecutive_errors: " + new_consecutive_errors + " new_test_request_to_go: " + new_test_request_to_go + " new_timestamp_cb_trips: " + new_timestamp_cb_trips + " ");
		}
	}

	public void run() {
		while(true) {
			Boolean microservice_response = Utilities.someVal(new BSet<Boolean>(true, false));
			Integer new_circuit_breaker = Utilities.someVal(new BSet<Integer>((new Enumerated(1,Utilities.max_integer))));
			Integer new_consecutive_errors = Utilities.someVal(new BSet<Integer>((new Enumerated(1,Utilities.max_integer))));
			Integer new_test_request_to_go = Utilities.someVal(new BSet<Integer>((new Enumerated(1,Utilities.max_integer))));
			Integer new_timestamp_cb_trips = Utilities.someVal(new BSet<Integer>((new Enumerated(1,Utilities.max_integer))));
			machine.lock.lock(); // start of critical section
			run_request(microservice_response,new_circuit_breaker,new_consecutive_errors,new_test_request_to_go,new_timestamp_cb_trips);
			machine.lock.unlock(); // end of critical section
		}
	}
}
