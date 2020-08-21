package uy.fing.edu.svergara.xml2junit.model.freemarker;

import java.util.ArrayList;
import java.util.List;

public class FreemarkerWrapperModel {

	private List<Operation> operations;
	private String packageName;

	public List<Operation> getOperations() {
		if (operations == null) {
			operations = new ArrayList<>();
		}
		return operations;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setOperations(List<Operation> operations) {
		this.operations = operations;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
}
