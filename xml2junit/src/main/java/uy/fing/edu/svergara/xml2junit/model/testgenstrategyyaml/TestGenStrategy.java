package uy.fing.edu.svergara.xml2junit.model.testgenstrategyyaml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestGenStrategy {

	private String artifactId;
	private String groupId;
	private String projectLocation;
	private List<Type> types;
	private List<Variable> variables;

	public Map<String, Variable> buildVariablesMap() {
		Map<String, Variable> variableMap = new HashMap<>() {

			private static final long serialVersionUID = -2461586418332178719L;

			@Override
			public Variable get(Object key) {
				Variable toReturn = super.get(key);
				if (toReturn == null) {
					toReturn = new Variable();
					toReturn.setName((String) key);
					this.put((String) key, toReturn);
				}
				return toReturn;
			}

		};
		for (Variable aVariable : getVariables()) {
			variableMap.put(aVariable.getName(), aVariable);
		}
		return variableMap;
	}

	public String getArtifactId() {
		return artifactId;
	}

	public String getGroupId() {
		return groupId;
	}

	public String getProjectLocation() {
		return projectLocation;
	}

	public List<Type> getTypes() {
		if (types == null) {
			types = new ArrayList<>();
		}
		return types;
	}

	public List<Variable> getVariables() {
		if (variables == null) {
			variables = new ArrayList<>();
		}
		return variables;
	}

	public void setArtifactId(String artifactId) {
		this.artifactId = artifactId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public void setProjectLocation(String projectLocation) {
		this.projectLocation = projectLocation;
	}

	public void setTypes(List<Type> types) {
		this.types = types;
	}

	public void setVariables(List<Variable> variables) {
		this.variables = variables;
	}

	@Override
	public String toString() {
		return "TestGenStrategy [artifactId=" + getArtifactId() + ", groupId=" + getGroupId() + ", projectLocation="
				+ getProjectLocation() + ", types=" + getTypes() + ", variables=" + getVariables() + "]";
	}

}
