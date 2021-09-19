package uy.edu.fing.svergara.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

public class ServiceRegistryUtil {

	public static final ServiceRegistryUtil instance = new ServiceRegistryUtil();
	private Integer minPort = 8096;
	private Map<String, Integer> endpointxport = new HashMap<>();
	private Map<String, Process> endpointxprocess = new HashMap<>();

	private ServiceRegistryUtil() {
	}

	private void buildApplicationYaml(String filePath, String serviceName, Integer servicePort)
			throws UnsupportedEncodingException, FileNotFoundException {

		Map<String, Object> serviceUrl = new HashMap<>();
		serviceUrl.put("defaultZone", "http://localhost:8092/eureka/");
		Map<String, Object> healthcheck = new HashMap<>();
		healthcheck.put("enabled", true);

		Map<String, Object> application = new HashMap<>();
		application.put("name", serviceName);
		Map<String, Object> client = new HashMap<>();
		client.put("serviceUrl", serviceUrl);
		client.put("healthcheck", healthcheck);

		Map<String, Object> spring = new HashMap<>();
		spring.put("application", application);
		Map<String, Object> server = new HashMap<>();
		server.put("port", servicePort);
		Map<String, Object> eureka = new HashMap<>();
		eureka.put("client", client);

		Map<String, Object> applicationYaml = new HashMap<>();
		applicationYaml.put("spring", spring);
		applicationYaml.put("server", server);
		applicationYaml.put("eureka", eureka);

		Writer writer = new OutputStreamWriter(new FileOutputStream(filePath), "UTF-8");

		DumperOptions options = new DumperOptions();
		options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
		options.setPrettyFlow(true);

		Yaml yaml = new Yaml(options);
		yaml.dump(applicationYaml, writer);
	}

	public void registerEndpoint(String serviceName, String serviceEndpoint) throws IOException, InterruptedException {
		String filePath = serviceName + "-" + serviceEndpoint + ".yaml";
		filePath.replaceAll(" ", "");
		if (!endpointxport.containsKey(serviceEndpoint)) {
			endpointxport.put(serviceEndpoint, minPort++);
		}
		buildApplicationYaml(filePath, serviceName, endpointxport.get(serviceEndpoint));
		Process process = runJar(filePath);
		endpointxprocess.put(serviceEndpoint, process);
	}

	private Process runJar(String springConfigFilePath) throws IOException, InterruptedException {
		File springConfigFile = new File(springConfigFilePath);
		ProcessBuilder processBuilder = new ProcessBuilder("java", "-jar",
				"service-registry-eureka-service-0.0.1-SNAPSHOT.jar",
				"--spring.config.location=file://" + springConfigFile.getAbsolutePath());
		processBuilder.inheritIO();
		Process process = processBuilder.start();
		Thread.sleep(10 * 1000); // time for the service to start and register
		System.out.println("process " + process.pid() + " is alive=" + process.isAlive());
		return process;
	}

	public void removeEndpoint(String a_service, String an_endpoint) throws InterruptedException {
		Process process = endpointxprocess.get(an_endpoint);
		process.destroy();
		endpointxprocess.remove(an_endpoint);
		Thread.sleep(10 * 1000); // time for the service to deregister
	}

	@SuppressWarnings("unchecked")
	public Map<String, Integer> getMappingEndpointxPort() {
		return (Map<String, Integer>) ((HashMap<String, Integer>) this.endpointxport).clone();
	}

	public void reset() {
		this.endpointxport = new HashMap<>();
		this.endpointxprocess = new HashMap<>();
	}
}
