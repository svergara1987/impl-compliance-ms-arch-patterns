package uy.edu.fing.svergara;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Endpoints extends java.util.HashMap<String, SetString> {

	private static final long serialVersionUID = 1L;

	public static Endpoints parse(String toParse) {
		// "{(S2|->{E1,E2}),(S3|->{E3}),(S4|->{E5})}"
		Stack<Character> stack = new Stack<Character>();
		List<String> tuples = new ArrayList<>();
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < toParse.length(); i++) {
			if (toParse.charAt(i) == '{' || toParse.charAt(i) == '(') {
				stack.push(toParse.charAt(i));
			} else if (toParse.charAt(i) == '}') {
				stack.pop();
			} else if (toParse.charAt(i) == ')') {
				stack.pop();
				tuples.add(stringBuilder.toString());
				stringBuilder = new StringBuilder();
			} else if (toParse.charAt(i) == ',') {
				if (stack.size() != 1)
					stringBuilder.append(toParse.charAt(i));
			} else if (toParse.charAt(i) != '-' && toParse.charAt(i) != '>') {
				stringBuilder.append(toParse.charAt(i));
			}
		}
		Endpoints endpointsToReturn = new Endpoints();
		String[] tupleElements, theEndpoints;
		String theService;
		SetString setEndpoints;
		for (String aTuple : tuples) {
			tupleElements = aTuple.split("[|]");
			theService = tupleElements[0];
			setEndpoints = new SetString();
			if (tupleElements.length > 1) {
				theEndpoints = tupleElements[1].split(",");
				for (String anEndpoint : theEndpoints) {
					setEndpoints.add(anEndpoint);
				}
			}
			endpointsToReturn.put(theService, setEndpoints);
		}
		return endpointsToReturn;
	}
}
