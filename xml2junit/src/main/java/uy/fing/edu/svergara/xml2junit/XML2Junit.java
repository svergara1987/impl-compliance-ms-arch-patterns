package uy.fing.edu.svergara.xml2junit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.StringWriter;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;
import uy.fing.edu.svergara.xml2junit.model.ExtendedTestSuite;
import uy.fing.edu.svergara.xml2junit.model.TestGenStrategy;
import uy.fing.edu.svergara.xml2junit.model.Type;
import uy.fing.edu.svergara.xml2junit.model.Variable;

public class XML2Junit {

	private static final String VELOCITY_TEMPLATE = "mytemplate.vm";

	public static void main(String[] args) {
		ArgumentParser parser = ArgumentParsers.newFor(XML2Junit.class.getSimpleName()).build().defaultHelp(true)
				.description("Generates JUnit test cases from custom test generation strategy");
		parser.addArgument("-s", "--strategy").help("test generation strategy yaml file. Defaults to test-gen.yaml")
				.setDefault("test-gen.yaml");
		parser.addArgument("-t", "--testcases").help("test cases xml file").required(true);
		try {
			Namespace ns = parser.parseArgs(args);
			new XML2Junit().execute(ns.getString("testcases"), ns.getString("strategy"));
		} catch (ArgumentParserException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (JAXBException e) {
			e.printStackTrace();
			System.exit(2);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(3);
		}
	}

	public void execute(String yamlTestGenStrategyPath, String xmlTestCasesPath)
			throws JAXBException, FileNotFoundException {
		xmlTestCasesPath = "/Users/svergara/Box/personales/fing/tesis de maestria/eventb_compliance/eventb2xml/test/20200718T1900_test_cases.xml";
		yamlTestGenStrategyPath = "/Users/svergara/Box/personales/fing/tesis de maestria/eventb_compliance/eventb2xml/test/test-gen.yaml";
		// TODO delete both previous hardcoded values
		TestGenStrategy testGenStrategy = parseYamlTestGenStrategyPath(yamlTestGenStrategyPath);
		ExtendedTestSuite extendedTestSuite = parseTestCasesXml(xmlTestCasesPath);
		generateJunit(extendedTestSuite);
	}

	private void generateJunit(ExtendedTestSuite extendedTestSuite) {
		VelocityEngine velocityEngine = new VelocityEngine();
		velocityEngine.init();
		VelocityContext context = new VelocityContext();
		context.put("className", new String("Wrapper"));
		context.put("extendedTestSuite", extendedTestSuite);
		Template template = velocityEngine.getTemplate(VELOCITY_TEMPLATE);
		StringWriter sw = new StringWriter();
		template.merge(context, sw);
	}

	private ExtendedTestSuite parseTestCasesXml(String xmlStrategyFilePath) throws JAXBException {
		File file = new File(xmlStrategyFilePath);
		JAXBContext jaxbContext = JAXBContext.newInstance(ExtendedTestSuite.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		return (ExtendedTestSuite) jaxbUnmarshaller.unmarshal(file);
	}

	private TestGenStrategy parseYamlTestGenStrategyPath(String yamlTestGenStrategy) throws FileNotFoundException {
		final Path yamlTestGenStrategyPath = Paths.get(yamlTestGenStrategy);
		Constructor constructor = new Constructor(TestGenStrategy.class);
		TypeDescription aTypeDescription = new TypeDescription(TestGenStrategy.class);
		aTypeDescription.addPropertyParameters("types", Type.class);
		aTypeDescription.addPropertyParameters("variables", Variable.class);
		constructor.addTypeDescription(aTypeDescription);
		aTypeDescription = new TypeDescription(Type.class);
		aTypeDescription.addPropertyParameters("values", String.class);
		constructor.addTypeDescription(aTypeDescription);
		aTypeDescription = new TypeDescription(Variable.class);
		aTypeDescription.addPropertyParameters("ignore", Boolean.class);
		constructor.addTypeDescription(aTypeDescription);
		Yaml yaml = new Yaml(constructor);
		FileInputStream fileInputStream = new FileInputStream(
				new File(yamlTestGenStrategyPath.toAbsolutePath().toString()));
		TestGenStrategy testGenStrategy = yaml.load(fileInputStream);
		return testGenStrategy;

	}
}
