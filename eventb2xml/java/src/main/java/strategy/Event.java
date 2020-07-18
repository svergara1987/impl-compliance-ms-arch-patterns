package strategy;

import java.util.ArrayList;
import java.util.List;

public class Event {

	private String eventName;
	private List<String> predicates;

	public String getEventName() {
		return eventName;
	}

	public List<String> getPredicates() {
		if (predicates == null) {
			predicates = new ArrayList<String>();
		}
		return predicates;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public void setPredicates(List<String> predicates) {
		this.predicates = predicates;
	}

	@Override
	public String toString() {
		return "Event [eventName=" + eventName + ", predicates=" + predicates + "]";
	}

}
