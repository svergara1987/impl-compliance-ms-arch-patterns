package circuit_breaker_pattern_multi_threaded;

//@ model import org.jmlspecs.models.JMLObjectSet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import Util.Utilities;
import eventb_prelude.BSet;
import eventb_prelude.Enumerated;
import eventb_prelude.NAT;
import eventb_prelude.NAT1;

public class m0_circuit_breaker{
	private int n_events = 2;
	private static final Integer max_integer = Utilities.max_integer;
	private static final Integer min_integer = Utilities.min_integer;
	private Thread[] events;
	public Lock lock = new ReentrantLock(true);


	/******Set definitions******/
	//@ public static constraint CIRCUIT_BREAKER_STATE.equals(\old(CIRCUIT_BREAKER_STATE)); 
	public static final BSet<Integer> CIRCUIT_BREAKER_STATE = new Enumerated(min_integer,max_integer);


	/******Constant definitions******/
	//@ public static constraint AMOUNT_TEST_REQUESTS.equals(\old(AMOUNT_TEST_REQUESTS)); 
	public static final Integer AMOUNT_TEST_REQUESTS = Test_m0_circuit_breaker.random_AMOUNT_TEST_REQUESTS;

	//@ public static constraint CLOSED.equals(\old(CLOSED)); 
	public static final Integer CLOSED = Test_m0_circuit_breaker.random_CLOSED;

	//@ public static constraint HALF_OPEN.equals(\old(HALF_OPEN)); 
	public static final Integer HALF_OPEN = Test_m0_circuit_breaker.random_HALF_OPEN;

	//@ public static constraint OPEN.equals(\old(OPEN)); 
	public static final Integer OPEN = Test_m0_circuit_breaker.random_OPEN;

	//@ public static constraint THRESHOLD.equals(\old(THRESHOLD)); 
	public static final Integer THRESHOLD = Test_m0_circuit_breaker.random_THRESHOLD;

	//@ public static constraint TIMEOUT_PERIOD.equals(\old(TIMEOUT_PERIOD)); 
	public static final Integer TIMEOUT_PERIOD = Test_m0_circuit_breaker.random_TIMEOUT_PERIOD;



	/******Axiom definitions******/
	/*@ public static invariant NAT1.instance.has(THRESHOLD); */
	/*@ public static invariant NAT1.instance.has(TIMEOUT_PERIOD); */
	/*@ public static invariant BSet.partition(CIRCUIT_BREAKER_STATE,new BSet<Integer>(OPEN),new BSet<Integer>(CLOSED),new BSet<Integer>(HALF_OPEN)); */
	/*@ public static invariant NAT1.instance.has(AMOUNT_TEST_REQUESTS); */
//	/*@ public static invariant THRESHOLD.equals(new Integer(3)); */
//	/*@ public static invariant TIMEOUT_PERIOD.equals(new Integer(2)); */
//	/*@ public static invariant AMOUNT_TEST_REQUESTS.equals(new Integer(3)); */


	/******Variable definitions******/
	/*@ spec_public */ private Integer circuit_breaker;

	/*@ spec_public */ private Integer consecutive_errors;

	/*@ spec_public */ private Integer test_request_to_go;

	/*@ spec_public */ private Integer time;

	/*@ spec_public */ private Integer timestamp_cb_trips;




	/******Invariant definition******/
	/*@ public invariant
		NAT.instance.has(time) &&
		NAT.instance.has(timestamp_cb_trips) &&
		NAT.instance.has(consecutive_errors) &&
		CIRCUIT_BREAKER_STATE.has(circuit_breaker) &&
		NAT.instance.has(test_request_to_go); */


	/******Getter and Mutator methods definition******/
	/*@ public normal_behavior
	    requires true;
	    assignable \nothing;
	    ensures \result == this.test_request_to_go;*/
	public /*@ pure */ Integer get_test_request_to_go(){
		return this.test_request_to_go;
	}

	/*@ public normal_behavior
	    requires true;
	    assignable this.test_request_to_go;
	    ensures this.test_request_to_go == test_request_to_go;*/
	public void set_test_request_to_go(Integer test_request_to_go){
		this.test_request_to_go = test_request_to_go;
	}

	/*@ public normal_behavior
	    requires true;
	    assignable \nothing;
	    ensures \result == this.timestamp_cb_trips;*/
	public /*@ pure */ Integer get_timestamp_cb_trips(){
		return this.timestamp_cb_trips;
	}

	/*@ public normal_behavior
	    requires true;
	    assignable this.timestamp_cb_trips;
	    ensures this.timestamp_cb_trips == timestamp_cb_trips;*/
	public void set_timestamp_cb_trips(Integer timestamp_cb_trips){
		this.timestamp_cb_trips = timestamp_cb_trips;
	}

	/*@ public normal_behavior
	    requires true;
	    assignable \nothing;
	    ensures \result == this.consecutive_errors;*/
	public /*@ pure */ Integer get_consecutive_errors(){
		return this.consecutive_errors;
	}

	/*@ public normal_behavior
	    requires true;
	    assignable this.consecutive_errors;
	    ensures this.consecutive_errors == consecutive_errors;*/
	public void set_consecutive_errors(Integer consecutive_errors){
		this.consecutive_errors = consecutive_errors;
	}

	/*@ public normal_behavior
	    requires true;
	    assignable \nothing;
	    ensures \result == this.circuit_breaker;*/
	public /*@ pure */ Integer get_circuit_breaker(){
		return this.circuit_breaker;
	}

	/*@ public normal_behavior
	    requires true;
	    assignable this.circuit_breaker;
	    ensures this.circuit_breaker == circuit_breaker;*/
	public void set_circuit_breaker(Integer circuit_breaker){
		this.circuit_breaker = circuit_breaker;
	}

	/*@ public normal_behavior
	    requires true;
	    assignable \nothing;
	    ensures \result == this.time;*/
	public /*@ pure */ Integer get_time(){
		return this.time;
	}

	/*@ public normal_behavior
	    requires true;
	    assignable this.time;
	    ensures this.time == time;*/
	public void set_time(Integer time){
		this.time = time;
	}



	/*@ public normal_behavior
	    requires true;
	    assignable \everything;
	    ensures
		time == 0 &&
		timestamp_cb_trips == 0 &&
		consecutive_errors == 0 &&
		circuit_breaker == CLOSED &&
		test_request_to_go == AMOUNT_TEST_REQUESTS;*/
	public m0_circuit_breaker(){
		time = 0;
		timestamp_cb_trips = 0;
		consecutive_errors = 0;
		circuit_breaker = CLOSED;
		test_request_to_go = AMOUNT_TEST_REQUESTS;

		events = new Thread[n_events];
		events[0] = new request(this);
		events[1] = new clock(this);

		for (int i = 0; i < n_events;i++){
			events[i].start();
		}
	}
}