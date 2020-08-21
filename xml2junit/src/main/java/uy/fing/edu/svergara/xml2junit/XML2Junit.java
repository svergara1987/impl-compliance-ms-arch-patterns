package uy.fing.edu.svergara.xml2junit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;
import uy.fing.edu.svergara.xml2junit.model.freemarker.FreeMarkerModelBuilderFactory;
import uy.fing.edu.svergara.xml2junit.model.testcasesxml.ExtendedTestSuite;
import uy.fing.edu.svergara.xml2junit.model.testgenstrategyyaml.TestGenStrategy;
import uy.fing.edu.svergara.xml2junit.model.testgenstrategyyaml.Type;
import uy.fing.edu.svergara.xml2junit.model.testgenstrategyyaml.Variable;

public class XML2Junit {

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
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(3);
		} catch (TemplateException e) {
			e.printStackTrace();
			System.exit(4);
		}
	}

	public void execute(String yamlTestGenStrategyPath, String xmlTestCasesPath)
			throws JAXBException, IOException, TemplateException {
		xmlTestCasesPath = "/Users/svergara/Box/personales/fing/tesis de maestria/eventb_compliance/eventb2xml/test/20200718T1900_test_cases.xml";
		yamlTestGenStrategyPath = "/Users/svergara/Desktop/tesis-maestria-eventb-compliance/test/test-gen.yaml";
		// TODO delete both previous hardcoded values
		TestGenStrategy testGenStrategy = parseYamlTestGenStrategyPath(yamlTestGenStrategyPath);
		ExtendedTestSuite extendedTestSuite = parseTestCasesXml(xmlTestCasesPath);
		generateJunit(testGenStrategy, extendedTestSuite);
	}

	private void generateJunit(TestGenStrategy testGenStrategy, ExtendedTestSuite extendedTestSuite)
			throws IOException, TemplateException {
		Configuration configuration = setupFreemarker();
		Writer out = new OutputStreamWriter(System.out);
		System.out.println("--------------------------------------------------------");
		Template wrapperTemplate = configuration.getTemplate("Wrapper.ftl");
		wrapperTemplate.process(FreeMarkerModelBuilderFactory.instance()
				.buildFreemarkerWrapperDataModel(testGenStrategy, extendedTestSuite), out);
		System.out.println("--------------------------------------------------------");
		Template enumTemplate = configuration.getTemplate("Enum.ftl");
		for (Type aType : testGenStrategy.getTypes()) {
			if ("enum".equalsIgnoreCase(aType.getSupertype())) {
				enumTemplate.process(
						FreeMarkerModelBuilderFactory.instance().buildFreemarkerEnumrDataModel(testGenStrategy, aType),
						out);
			} else {
				throw new RuntimeException(aType.getName() + " supertype attribute is not valid");
			}
		}
		System.out.println("--------------------------------------------------------");
		Template wrapperTestTemplate = configuration.getTemplate("WrapperTest.ftl");
		wrapperTestTemplate.process(FreeMarkerModelBuilderFactory.instance()
				.buildFreemarkerWrapperTestDataModel(testGenStrategy, extendedTestSuite), out);
		System.out.println("--------------------------------------------------------");
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

	private Configuration setupFreemarker() throws IOException {
		Configuration configuration = new Configuration(Configuration.VERSION_2_3_30);
		configuration.setDirectoryForTemplateLoading(new File("resources/freemarker/templates"));
		configuration.setDefaultEncoding("UTF-8");
		configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
		configuration.setLogTemplateExceptions(false);
		configuration.setWrapUncheckedExceptions(true);
		configuration.setWrapUncheckedExceptions(true);
		return configuration;
	}
}
