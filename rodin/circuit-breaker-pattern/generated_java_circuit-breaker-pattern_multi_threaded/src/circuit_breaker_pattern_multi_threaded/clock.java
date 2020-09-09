package circuit_breaker_pattern_multi_threaded; 

import eventb_prelude.*;
import Util.Utilities;

public class clock extends Thread{
	/*@ spec_public */ private m0_circuit_breaker machine; // reference to the machine 

	/*@ public normal_behavior
		requires true;
		assignable \everything;
		ensures this.machine == m; */
	public clock(m0_circuit_breaker m) {
		this.machine = m;
	}

	/*@ public normal_behavior
		requires true;
 		assignable \nothing;
		ensures \result <==> (machine.CIRCUIT_BREAKER_STATE.has(new_circuit_breaker) && ((machine.get_circuit_breaker().equals(machine.OPEN) && new Integer(new Integer(machine.get_time() + new Integer(1)) - machine.get_timestamp_cb_trips()).equals(machine.TIMEOUT_PERIOD)) ==> (new_circuit_breaker.equals(machine.HALF_OPEN))) && ((machine.get_circuit_breaker().equals(machine.OPEN) && !new Integer(new Integer(machine.get_time() + new Integer(1)) - machine.get_timestamp_cb_trips()).equals(machine.TIMEOUT_PERIOD)) ==> (new_circuit_breaker.equals(machine.get_circuit_breaker()))) && ((machine.get_circuit_breaker().equals(machine.CLOSED) || machine.get_circuit_breaker().equals(machine.HALF_OPEN)) ==> (new_circuit_breaker.equals(machine.get_circuit_breaker())))); */
	public /*@ pure */ boolean guard_clock( Integer new_circuit_breaker) {
		return (machine.CIRCUIT_BREAKER_STATE.has(new_circuit_breaker) && BOOL.implication(machine.get_circuit_breaker().equals(machine.OPEN) && new Integer(new Integer(machine.get_time() + new Integer(1)) - machine.get_timestamp_cb_trips()).equals(machine.TIMEOUT_PERIOD),new_circuit_breaker.equals(machine.HALF_OPEN)) && BOOL.implication(machine.get_circuit_breaker().equals(machine.OPEN) && !new Integer(new Integer(machine.get_time() + new Integer(1)) - machine.get_timestamp_cb_trips()).equals(machine.TIMEOUT_PERIOD),new_circuit_breaker.equals(machine.get_circuit_breaker())) && BOOL.implication(machine.get_circuit_breaker().equals(machine.CLOSED) || machine.get_circuit_breaker().equals(machine.HALF_OPEN),new_circuit_breaker.equals(machine.get_circuit_breaker())));
	}

	/*@ public normal_behavior
		requires guard_clock(new_circuit_breaker);
		assignable machine.time, machine.circuit_breaker;
		ensures guard_clock(new_circuit_breaker) &&  machine.get_time() == \old(new Integer(machine.get_time() + 1)) &&  machine.get_circuit_breaker() == \old(new_circuit_breaker); 
	 also
		requires !guard_clock(new_circuit_breaker);
		assignable \nothing;
		ensures true; */
	public void run_clock( Integer new_circuit_breaker){
		if(guard_clock(new_circuit_breaker)) {
			Integer time_tmp = machine.get_time();
			Integer circuit_breaker_tmp = machine.get_circuit_breaker();

			machine.set_time(new Integer(time_tmp + 1));
			machine.set_circuit_breaker(new_circuit_breaker);

			System.out.println("clock executed new_circuit_breaker: " + new_circuit_breaker + " ");
		}
	}

	public void run() {
		while(true) {
			Integer new_circuit_breaker = Utilities.someVal(new BSet<Integer>((new Enumerated(1,Utilities.max_integer))));
			machine.lock.lock(); // start of critical section
			run_clock(new_circuit_breaker);
			machine.lock.unlock(); // end of critical section
		}
	}
}
