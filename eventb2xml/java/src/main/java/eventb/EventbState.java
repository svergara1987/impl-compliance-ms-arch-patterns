package eventb;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import de.prob.animator.domainobjects.FormulaExpand;
import de.prob.statespace.LoadedMachine;
import de.prob.statespace.StateSpace;
import de.prob.statespace.Trace;

public class EventbState {

	private Map<String, String> constants;
	private String eventExecuted;
	private StateSpace stateSpace;
	private Trace trace;
	private Map<String, String> variables;

	public EventbState() {
		super();
	}

	private void extractConstants() {
		constants = new HashMap<>();
		LoadedMachine machine = trace.getStateSpace().getLoadedMachine();
		String constantName = null, constantValue = null;
		for (Iterator<String> iterConstantNames = machine.getConstantNames().iterator(); iterConstantNames.hasNext();) {
			constantName = iterConstantNames.next();
			constantValue = trace.getCurrentState().eval(constantName, FormulaExpand.EXPAND).toString();
			constants.put(constantName, constantValue);
		}
	}

	private void extractVariables() {
		variables = new HashMap<>();
		LoadedMachine machine = trace.getStateSpace().getLoadedMachine();
		String variableName = null, variableValue = null;
		for (Iterator<String> iterVariablesNames = machine.getVariableNames().iterator(); iterVariablesNames
				.hasNext();) {
			variableName = iterVariablesNames.next();
			variableValue = trace.getCurrentState().eval(variableName, FormulaExpand.EXPAND).toString();
			variables.put(variableName, variableValue);
		}
	}

	public Map<String, String> getConstants() {
		if (constants == null) {
			extractConstants();
		}
		return constants;
	}

	public String getEventExecuted() {
		return eventExecuted;
	}

	public StateSpace getStateSpace() {
		return stateSpace;
	}

	public Trace getTrace() {
		return trace;
	}

	public Map<String, String> getVariables() {
		if (variables == null) {
			extractVariables();
		}
		return variables;
	}

	public void setEventExecuted(String eventExecuted) {
		this.eventExecuted = eventExecuted;
	}

	public void setStateSpace(StateSpace stateSpace) {
		this.stateSpace = stateSpace;
	}

	public void setTrace(Trace trace) {
		this.trace = trace;
	}

	@Override
	public String toString() {
		getConstants();
		getVariables();
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(this.getClass()).append("\n");
		Field[] fields = this.getClass().getDeclaredFields();
		Object fieldValue = null;
		for (int i = 0; i < fields.length; i++) {
			fields[i].setAccessible(true);
			try {
				fieldValue = fields[i].get(this);
				stringBuilder.append("- ").append(fields[i].getName()).append("=")
						.append(fieldValue != null ? fieldValue.toString() : "null").append("\n");
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return stringBuilder.toString();
	}

}
