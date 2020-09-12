package uy.fing.edu.svergara.xml2junit.model.freemarker;

public class FreemarkerTypeModel {

	private String className;
	private String extendsFrom;
	private String packageName;

	public String getClassName() {
		return className;
	}

	public String getExtendsFrom() {
		if (extendsFrom == null) {
			extendsFrom = Object.class.getSimpleName();
		}
		return extendsFrom;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public void setExtendsFrom(String extendsFrom) {
		this.extendsFrom = extendsFrom;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	@Override
	public String toString() {
		return "FreemarkerTypeModel [className=" + className + ", packageName=" + packageName + ", extendsFrom="
				+ extendsFrom + "]";
	}

}
