package uy.edu.fing.svergara;

public class Status {

	private String open;

	public Status() {

	}

	public String getOpen() {
		return open;
	}

	public boolean isOpen() {
		return Boolean.getBoolean(open);
	}

	public void setOpen(String open) {
		this.open = open;
	}

}
