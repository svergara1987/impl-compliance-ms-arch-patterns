import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Stage;

import de.prob.animator.domainobjects.FormulaExpand;
import de.prob.cli.CliVersionNumber;
import de.prob.scripting.Api;
import de.prob.scripting.ModelTranslationError;
import de.prob.statespace.Trace;
import de.prob.statespace.Transition;
import eventb.EventbState;
import eventb.InvalidNextEvent;
import logging.CustomStrategyLogger;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;
import strategy.Event;
import strategy.Strategy;

public class ProbCliCustomStrategy {

	private static Injector INJECTOR = Guice.createInjector(Stage.PRODUCTION, new MyGuiceConfig());
	private final Api api;
	private final static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	private EventbState current;
	private List<EventbState> history;
	private Strategy strategy;

	@Inject
	public ProbCliCustomStrategy(final Api api) {
		this.api = api;
		history = new ArrayList<>();
	}

	private CliVersionNumber getProbVersion() {
		CliVersionNumber apiVersion = api.getVersion();
		logger.fine("ProB version: " + apiVersion);
		return apiVersion;
	}

	private void setUpExecutionEnvironment(final String eventbMachineFile, final String testStrategyFile)
			throws URISyntaxException, IOException, ModelTranslationError, InvalidNextEvent {
		getProbVersion();
//		int index = 1;
		logger.fine("Parsing strategy file");
		// https://www.baeldung.com/java-snake-yaml
		final Path testStrategyPath = Paths.get(testStrategyFile);
		Constructor constructor = new Constructor(Strategy.class);
		TypeDescription aTypeDescription = new TypeDescription(Strategy.class);
		aTypeDescription.addPropertyParameters("expression", String.class);
		aTypeDescription.addPropertyParameters("operations", String.class);
		constructor.addTypeDescription(aTypeDescription);
		Yaml yaml = new Yaml(constructor);
		FileInputStream fileInputStream = new FileInputStream(new File(testStrategyPath.toAbsolutePath().toString()));
//		in case more than one document in same file 
//		for (Object aDocument : yaml.loadAll(fileInputStream)) {
//			logger.fine(index + ") " + aDocument);
//			if (aDocument instanceof Strategy) {
//				strategies.add((Strategy) aDocument);
//				index++;
//			} else {
//				logger.severe("parsing error: last document is not instance of Strategy");
//				fileInputStream.close();
//				throw new IOException("error when parsing strategy file with path '" + testStrategyFile + "'");
//			}
//		}
		strategy = yaml.load(fileInputStream);
		logger.fine("Loading Event-B machine");
		// https://github.com/bendisposto/prob2_tooling_template
		// https://www3.hhu.de/stups/handbook/prob2/prob_handbook.html#evaluation
		// https://groups.google.com/forum/#!searchin/prob-users/evaluate%7Csort:date/prob-users/UCSLEyJAy0w/utGUpkQmAgAJ
		final Path eventbMachinePath = Paths.get(eventbMachineFile);
		if (eventbMachinePath == null) {
			logger.severe("parsing error: could not load event-b machine");
			throw new IOException("error when loading event-b machine path '" + eventbMachineFile + "'");
		}
		current = new EventbState();
		current.setStateSpace(api.eventb_load(eventbMachinePath.toAbsolutePath().toString()));
		current.setTrace(new Trace(current.getStateSpace()));
		logger.fine("Event-B machine loaded");
		executeEvent("$setup_constants");
		executeEvent("$initialise_machine");
		logger.fine("Execution environment set up");
	}

	private EventbState executeEvent(String eventName) throws InvalidNextEvent {
		return executeEvent(eventName, new ArrayList<String>());
	}

	private EventbState executeEvent(String eventName, List<String> predicates) throws InvalidNextEvent {
		Transition transition = current.getTrace().getCurrentState().findTransition(eventName, predicates);
		if (transition == null) {
			throw new InvalidNextEvent(eventName, predicates);
		}		
		EventbState previous = current;
		EventbState next = new EventbState();
		next.setTrace(previous.getTrace().execute(eventName, predicates));
		next.setStateSpace(next.getTrace().getStateSpace());
		next.setEventExecuted(eventName);
		history.add(next);
		current = next;
		logger.fine("Event executed: " + current.getTrace().getCurrent().getTransition().evaluate(FormulaExpand.EXPAND).getPrettyRep());
		if (!transition.isArtificialTransition()) {
			logger.fine("transition.getParameterNames = " + current.getStateSpace().getLoadedMachine()
					.getMachineOperationInfo(eventName).getParameterNames());
		}
		return previous;
	}

	private Event parseEvent(String unparsedEvent) throws EventParsingException {
		Pattern pattern = Pattern.compile("([a-z]+)([ ]*[(](.*)[)])?", Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(unparsedEvent);
		if (matcher.find()) {
			Event event = new Event();
			event.setEventName(matcher.group(1));
			if (matcher.group(3) != null) {
				event.getPredicates().addAll(Arrays.asList(matcher.group(3).split(",")));
			}
			return event;
		} else {
			logger.fine("error when parsing " + unparsedEvent);
			throw new EventParsingException("error when parsing " + unparsedEvent);
		}
	}

	public void executeCustomStrategy(final String machineFile, final String testStrategyFile, String outputFile)
			throws URISyntaxException, IOException, ModelTranslationError, InvalidNextEvent, EventParsingException,
			ParserConfigurationException, TransformerException {
		logger.fine(new StringBuilder().append("Starting executeCustomStrategy with parameters machineFile=")
				.append(machineFile).append(" testStrategyFile=").append(testStrategyFile).toString());
		setUpExecutionEnvironment(machineFile, testStrategyFile);
		Event event = null;
		for (String unparsedEvent : strategy.getEvents()) {
			event = parseEvent(unparsedEvent);
			executeEvent(event.getEventName(), event.getPredicates());
		}
		logger.fine(history.toString());
		generateXML(outputFile);
		logger.fine("executeCustomStrategy ended");
	}

	private void generateXML(String outputFile) throws ParserConfigurationException, TransformerException {
		logger.fine("generating xml output " + outputFile);
		DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
		Document document = documentBuilder.newDocument();
		logger.fine("creating extended_test_suite element");
		Element root = document.createElement("extended_test_suite");
		document.appendChild(root);
		logger.fine("creating test_case element");
		Element test_case = document.createElement("test_case");
		root.appendChild(test_case);
		EventbState anEvent = history.get(1);
		logger.fine("creating initialisation element");
		Element initialisation = document.createElement("initialisation");
		test_case.appendChild(initialisation);
		Element value = null;
		for (String aConstant : anEvent.getConstants().keySet()) {
			value = document.createElement("value");
			value.setAttribute("type", "constant");
			value.setAttribute("name", aConstant);
			value.appendChild(document.createTextNode(anEvent.getConstants().get(aConstant)));
			initialisation.appendChild(value);
		}
		for (String aVariable : anEvent.getVariables().keySet()) {
			value = document.createElement("value");
			value.setAttribute("type", "variable");
			value.setAttribute("name", aVariable);
			value.appendChild(document.createTextNode(anEvent.getVariables().get(aVariable)));
			initialisation.appendChild(value);
		}
		List<String> paramNames = null, paramValues = null;
		Map<String, String> modifiedVariables = null;
		Element step = null;
		for (int i = 2; i < history.size(); i++) {
			anEvent = history.get(i);
			logger.fine("creating step element " + anEvent.getEventExecuted());
			step = document.createElement("step");
			step.setAttribute("name", anEvent.getEventExecuted());
			test_case.appendChild(step);
			paramNames = anEvent.getStateSpace().getLoadedMachine().getMachineOperationInfo(anEvent.getEventExecuted()).getParameterNames();
			paramValues = anEvent.getTrace().getCurrent().getTransition().evaluate(FormulaExpand.EXPAND).getParameterValues();
			for (int paramNameIndex = 0; paramNameIndex < paramNames.size(); paramNameIndex++) {
				value = document.createElement("value");
				value.setAttribute("name", paramNames.get(paramNameIndex));
				value.appendChild(document.createTextNode(paramValues.get(paramNameIndex)));
				step.appendChild(value);
			}
			modifiedVariables = calculateModifiedVariables(anEvent, history.get(i - 1));
			for (String modifiedVariableKey : modifiedVariables.keySet()) {
				value = document.createElement("modified");
				value.setAttribute("name", modifiedVariableKey);
				value.appendChild(document.createTextNode(modifiedVariables.get(modifiedVariableKey)));
				step.appendChild(value);
			}
		}
		logger.fine("saving xml output in " + outputFile);
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
		transformer.setOutputProperty(OutputKeys.VERSION, "1.0");
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
        DOMSource domSource = new DOMSource(document);
        StreamResult streamResult = new StreamResult(new File(outputFile));
        transformer.transform(domSource, streamResult);
        logger.fine("done creating xml output file");
	}

	private Map<String, String> calculateModifiedVariables(EventbState current, EventbState previous) {
		Map<String, String> retorno = new HashMap<String, String>();
		for (String key : current.getVariables().keySet()) {
			if (!current.getVariables().get(key).equalsIgnoreCase(previous.getVariables().get(key))) {
				retorno.put(key, current.getVariables().get(key));
			}
		}
		return retorno;
	}

	// gradle run --args="--machine '/Users/svergara/Box/personales/fing/tesis de
	// maestria/probcli/svergara_test_generator/eventb2xml/eventb_machines/m0_circuit_breaker_mch.eventb'
	// --strategy '/Users/svergara/Box/personales/fing/tesis de
	// maestria/probcli/svergara_test_generator/eventb2xml/test/strategy.yaml'"
	public static void main(final String[] args) {
		final ProbCliCustomStrategy m = INJECTOR.getInstance(ProbCliCustomStrategy.class);
		ArgumentParser parser = ArgumentParsers.newFor(ProbCliCustomStrategy.class.getSimpleName()).build()
				.defaultHelp(true).description("Validate and generate XML from custom test strategy");
		parser.addArgument("-m", "--machine").required(true).help(".eventb machine file");
		parser.addArgument("-s", "--strategy").help("test strategy yaml file. Defaults to strategy.yaml")
				.setDefault("strategy.yaml");
		parser.addArgument("-o", "--output").required(true).help("output file to place result xml");
		Namespace ns = null;
		try {			
			for (int i = 0; i < args.length; i++) {
				System.out.println("args[" + i + "]=" + args[i]);
			}
			ns = parser.parseArgs(args);
			CustomStrategyLogger.setup();
			logger.fine("");
			logger.fine("--------------------------------------------------------------------------------------------");
			m.executeCustomStrategy(ns.getString("machine"), ns.getString("strategy"), ns.getString("output"));
			System.exit(0);
		} catch (ArgumentParserException e) {
			parser.handleError(e);
			System.exit(1);
		} catch (InvalidNextEvent e) {
			e.printStackTrace();
			System.exit(2);
		} catch (EventParsingException e) {
			e.printStackTrace();
			System.exit(3);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(4);
		}
	}
}