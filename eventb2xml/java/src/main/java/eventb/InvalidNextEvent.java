package eventb;

import java.util.List;

public class InvalidNextEvent extends Exception {

	private static final long serialVersionUID = -8192292147015327999L;
	private String eventName;
	private List<String> predicates;

	public InvalidNextEvent(String eventName, List<String> predicates) {
		this.eventName = eventName;
		this.predicates = predicates;
	}

	@Override
	public String toString() {
		return "InvalidNextEvent [eventName=" + eventName + ", predicates=" + predicates + "]";
	}

	@Override
	public String getMessage() {
		return this.toString();
	}

}
