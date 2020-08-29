package uy.fing.edu.svergara.xml2junit.model.freemarker;

import java.util.ArrayList;
import java.util.List;

public class JunitStep {

	private static Integer JUNIT_STEP_ID = 1;

	private final Integer id = JUNIT_STEP_ID++;
	private String methodName;
	private List<String> methodParameters;
	private List<JunitVariable> post = null;
	private List<JunitVariable> pre = null;

	public Integer getId() {
		return id;
	}

	public String getMethodName() {
		return methodName;
	}

	public List<String> getMethodParameters() {
		if (methodParameters == null) {
			methodParameters = new ArrayList<>();
		}
		return methodParameters;
	}

	public List<JunitVariable> getPost() {
		if (post == null) {
			post = new ArrayList<>();
		}
		return post;
	}

	public List<JunitVariable> getPre() {
		if (pre == null) {
			pre = new ArrayList<>();
		}
		return pre;
	}

	public JunitStep setMethodName(String methodName) {
		this.methodName = methodName;
		return this;
	}

	public void setMethodParameters(List<String> methodParameters) {
		this.methodParameters = methodParameters;
	}

	public void setPost(List<JunitVariable> post) {
		this.post = post;
	}

	public void setPre(List<JunitVariable> pre) {
		this.pre = pre;
	}

	@Override
	public String toString() {
		return "JunitStep [id=" + getId() + ", methodName=" + getMethodName() + ", methodParameters="
				+ getMethodParameters() + ", post=" + getPost() + ", pre=" + getPre() + "]";
	}

}
