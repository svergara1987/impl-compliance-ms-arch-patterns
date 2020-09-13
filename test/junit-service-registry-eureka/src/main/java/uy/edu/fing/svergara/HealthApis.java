package uy.edu.fing.svergara;

public class HealthApis extends java.util.HashMap<String, String> {

	private static final long serialVersionUID = 1L;

	public static HealthApis parse(String toParse) {
		// {(E1|->HCA3),(E3|->HCA2),(E4|->HCA3)}
		HealthApis healthApisToReturn = new HealthApis();
		String[] tupleElements;
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < toParse.length(); i++) {
			if (toParse.charAt(i) == ')') {
				tupleElements = stringBuilder.toString().split("[|]");
				healthApisToReturn.put(tupleElements[0], tupleElements[1]);
				stringBuilder = new StringBuilder();
			} else if (toParse.charAt(i) != '{' && toParse.charAt(i) != '(' && toParse.charAt(i) != ','
					&& toParse.charAt(i) != '-' && toParse.charAt(i) != '>') {
				stringBuilder.append(toParse.charAt(i));
			}
		}
		return healthApisToReturn;
	}
}
