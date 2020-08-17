package uy.fing.edu.svergara.xml2junit.model;

import java.util.List;

public class TestGenStrategy {

	private String artifactId;
	private String groupId;
	private String projectLocation;
	private List<Type> types;
	private List<Variable> variables;

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
		return types;
	}

	public List<Variable> getVariables() {
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
}
