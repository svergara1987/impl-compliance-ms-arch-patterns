package uy.fing.edu.svergara.xml2junit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
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
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;
import uy.fing.edu.svergara.xml2junit.model.freemarker.FreeMarkerModelBuilderFactory;
import uy.fing.edu.svergara.xml2junit.model.freemarker.FreemarkerWrapperModel;
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
			new XML2Junit().execute(ns.getString("strategy"), ns.getString("testcases"));
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
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.exit(5);
		}
	}

	public void execute(String yamlTestGenStrategyPath, String xmlTestCasesPath)
			throws JAXBException, IOException, TemplateException, InterruptedException {
		TestGenStrategy testGenStrategy = parseYamlTestGenStrategyPath(yamlTestGenStrategyPath);
		ExtendedTestSuite extendedTestSuite = parseTestCasesXml(xmlTestCasesPath);
		createProjectStructure(testGenStrategy);
		generateContents(testGenStrategy, extendedTestSuite);
	}

	private void createProjectStructure(TestGenStrategy testGenStrategy) throws IOException, InterruptedException {
		// generate default project structure
		ProcessBuilder processBuilder = new ProcessBuilder("mvn", "archetype:generate",
				"-DgroupId=" + testGenStrategy.getGroupId(), "-DartifactId=" + testGenStrategy.getArtifactId(),
				"-DinteractiveMode=false");
		processBuilder.directory(new File(testGenStrategy.getProjectLocation()));
		processBuilder.inheritIO();
		Process process = processBuilder.start();
		int exitCode = process.waitFor();
		if (exitCode != 0) {
			System.out.println("error when executing mvn command");
			throw new RuntimeException("error creating project structure");
		}
		// delete App.java
		StringBuilder appPath = new StringBuilder();
		appPath.append(testGenStrategy.getArtifactId()).append(File.separator);
		appPath.append("src").append(File.separator);
		appPath.append("main").append(File.separator);
		appPath.append("java").append(File.separator);
		appPath.append(testGenStrategy.getGroupId().replaceAll("[.]", File.separator)).append(File.separator);
		appPath.append("App.java");
		processBuilder = new ProcessBuilder("rm", "-f", appPath.toString());
		processBuilder.directory(new File(testGenStrategy.getProjectLocation()));
		processBuilder.inheritIO();
		process = processBuilder.start();
		exitCode = process.waitFor();
		if (exitCode != 0) {
			System.out.println("error when executing rm -f App.java");
			throw new RuntimeException("error creating project structure");
		}
		// delete AppTest.java
		StringBuilder appTestPath = new StringBuilder();
		appTestPath.append(testGenStrategy.getArtifactId()).append(File.separator);
		appTestPath.append("src").append(File.separator);
		appTestPath.append("test").append(File.separator);
		appTestPath.append("java").append(File.separator);
		appTestPath.append(testGenStrategy.getGroupId().replaceAll("[.]", File.separator)).append(File.separator);
		appTestPath.append("AppTest.java");
		processBuilder = new ProcessBuilder("rm", "-f", appTestPath.toString());
		processBuilder.directory(new File(testGenStrategy.getProjectLocation()));
		processBuilder.inheritIO();
		process = processBuilder.start();
		exitCode = process.waitFor();
		if (exitCode != 0) {
			System.out.println("error when executing rm -f AppTest.java");
			throw new RuntimeException("error creating project structure");
		}
	}

	private void generateContents(TestGenStrategy testGenStrategy, ExtendedTestSuite extendedTestSuite)
			throws IOException, TemplateException {
		Configuration configuration = setupFreemarker();
		System.out.println("Wrapper.java --------------------------------------------------------");
		StringBuilder wrapperPath = new StringBuilder();
		wrapperPath.append(testGenStrategy.getProjectLocation()).append(File.separator);
		wrapperPath.append(testGenStrategy.getArtifactId()).append(File.separator);
		wrapperPath.append("src").append(File.separator);
		wrapperPath.append("main").append(File.separator);
		wrapperPath.append("java").append(File.separator);
		wrapperPath.append(testGenStrategy.getGroupId().replaceAll("[.]", File.separator)).append(File.separator);
		wrapperPath.append("Wrapper.java");
		FreemarkerWrapperModel freemarkerWrapperModel = FreeMarkerModelBuilderFactory.instance()
				.buildFreemarkerWrapperDataModel(testGenStrategy, extendedTestSuite);
		configuration.getTemplate("Wrapper.ftl").process(freemarkerWrapperModel,
				new FileWriter(wrapperPath.toString()));
		System.out.println("Types --------------------------------------------------------");
		StringBuilder typePath = null;
		for (Type aType : testGenStrategy.getTypes()) {
			if ("enum".equalsIgnoreCase(aType.getSupertype())) {
				typePath = new StringBuilder();
				typePath.append(testGenStrategy.getProjectLocation()).append(File.separator);
				typePath.append(testGenStrategy.getArtifactId()).append(File.separator);
				typePath.append("src").append(File.separator);
				typePath.append("main").append(File.separator);
				typePath.append("java").append(File.separator);
				typePath.append(testGenStrategy.getGroupId().replaceAll("[.]", File.separator)).append(File.separator);
				typePath.append(aType.getName()).append(".java");
				configuration.getTemplate("Enum.ftl").process(
						FreeMarkerModelBuilderFactory.instance().buildFreemarkerEnumrDataModel(testGenStrategy, aType),
						new FileWriter(typePath.toString()));
			} else {
				throw new RuntimeException(aType.getName() + " supertype attribute is not valid");
			}
		}
		System.out.println("WrapperTest.java --------------------------------------------------------");
		StringBuilder wrapperTestPath = new StringBuilder();
		wrapperTestPath.append(testGenStrategy.getProjectLocation()).append(File.separator);
		wrapperTestPath.append(testGenStrategy.getArtifactId()).append(File.separator);
		wrapperTestPath.append("src").append(File.separator);
		wrapperTestPath.append("test").append(File.separator);
		wrapperTestPath.append("java").append(File.separator);
		wrapperTestPath.append(testGenStrategy.getGroupId().replaceAll("[.]", File.separator)).append(File.separator);
		wrapperTestPath.append("WrapperTest.java");
		configuration.getTemplate("WrapperTest.ftl").process(FreeMarkerModelBuilderFactory.instance()
				.buildFreemarkerWrapperTestDataModel(testGenStrategy, extendedTestSuite, freemarkerWrapperModel),
				new FileWriter(wrapperTestPath.toString()));
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
