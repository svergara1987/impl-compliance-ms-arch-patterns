package uy.fing.edu.svergara.xml2junit.model.freemarker;

import java.util.ArrayList;
import java.util.List;

public class FreemarkerEnumModel {

	private String enumName;
	private List<String> enumValues;
	private String packageName;

	public String getEnumName() {
		return enumName;
	}

	public List<String> getEnumValues() {
		if (enumValues == null) {
			enumValues = new ArrayList<>();
		}
		return enumValues;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setEnumName(String enumName) {
		this.enumName = enumName;
	}

	public void setEnumValues(List<String> enumValues) {
		this.enumValues = enumValues;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	@Override
	public String toString() {
		return "FreemarkerEnumModel [enumName=" + enumName + ", enumValues=" + enumValues + ", packageName="
				+ packageName + "]";
	}

}
