package uy.edu.fing.svergara;

public class HealthApiResponses extends java.util.HashMap<String, Boolean> {

	private static final long serialVersionUID = 1L;

	public static HealthApiResponses parse(String toParse) {
		// {(E1|->TRUE)}
		HealthApiResponses healthApisResponsesToReturn = new HealthApiResponses();
		String[] tupleElements;
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < toParse.length(); i++) {
			if (toParse.charAt(i) == ')') {
				tupleElements = stringBuilder.toString().split("[|]");
				healthApisResponsesToReturn.put(tupleElements[0], Boolean.parseBoolean(tupleElements[1]));
				stringBuilder = new StringBuilder();
			} else if (toParse.charAt(i) != '{' && toParse.charAt(i) != '(' && toParse.charAt(i) != ','
					&& toParse.charAt(i) != '-' && toParse.charAt(i) != '>') {
				stringBuilder.append(toParse.charAt(i));
			}
		}
		return healthApisResponsesToReturn;
	}
}
