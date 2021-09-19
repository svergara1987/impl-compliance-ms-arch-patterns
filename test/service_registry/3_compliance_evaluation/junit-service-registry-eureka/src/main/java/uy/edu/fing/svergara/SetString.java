package uy.edu.fing.svergara;

public class SetString extends java.util.HashSet<String> {

	private static final long serialVersionUID = 1L;

	public static SetString parse(String toParse) {
		// "{S1,S4,S3}"
		SetString setStringToReturn = new SetString();
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < toParse.length(); i++) {
			if (toParse.charAt(i) == ',') {
				setStringToReturn.add(stringBuilder.toString());
				stringBuilder = new StringBuilder();
			} else if (toParse.charAt(i) == '}') {
				if (stringBuilder.length() > 0) {
					setStringToReturn.add(stringBuilder.toString());
				}
			} else if (toParse.charAt(i) != '{' && toParse.charAt(i) != '-' && toParse.charAt(i) != '>') {
				stringBuilder.append(toParse.charAt(i));
			}
		}
		return setStringToReturn;
	}
}
