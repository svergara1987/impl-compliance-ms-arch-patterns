package uy.edu.fing.svergara;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import uy.edu.fing.svergara.utils.ServiceRegistryUtil;

public class Wrapper {

	private Endpoints endpoints;
	private SetString unavailableEndpoints;
	private SetString services;
	private Map<String, List<String>> hcaxendpoint;

	public void initialisation(Endpoints endpoints, HealthApis health_apis, SetString last_endpoint_active_query_result,
			SetString last_health_query_result, SetString services, SetString unavailable_endpoints) {
		this.endpoints = endpoints;
		this.unavailableEndpoints = unavailable_endpoints;
		this.services = services;
		ServiceRegistryUtil.instance.reset();
		hcaxendpoint = new HashMap<>();
	}

	public Boolean isValid(Endpoints endpoints, HealthApis health_apis, SetString last_endpoint_active_query_result,
			SetString last_health_query_result, SetString services, SetString unavailable_endpoints) {
		String requestURL = "http://localhost:8092/eureka/apps";
		OkHttpClient client = new OkHttpClient().newBuilder().build();
		Request request = new Request.Builder().url(requestURL).method("GET", null).build();
		try {
			Response response = client.newCall(request).execute();
			String responseStr = response.body().string();
			System.out.println("isValid responseStr \n" + responseStr);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(new InputSource(new StringReader(responseStr)));
			doc.getDocumentElement().normalize();
			Map<String, List<Integer>> servicexendpoints = new HashMap<>();
			NodeList allApplications = doc.getElementsByTagName("application");
			Element anApplication, anInstance;
			String appName;
			List<Integer> appEndpoints;
			NodeList applicationInstances;
			for (int i = 0; i < allApplications.getLength(); i++) {
				if (allApplications.item(i).getNodeType() == Node.ELEMENT_NODE) {
					anApplication = (Element) allApplications.item(i);
					appName = anApplication.getElementsByTagName("name").item(0).getTextContent();
					if (!servicexendpoints.containsKey(appName)) {
						servicexendpoints.put(appName, new ArrayList<>());
					}
					appEndpoints = servicexendpoints.get(appName);
					applicationInstances = anApplication.getElementsByTagName("instance");
					for (int j = 0; j < applicationInstances.getLength(); j++) {
						if (applicationInstances.item(j).getNodeType() == Node.ELEMENT_NODE) {
							anInstance = (Element) applicationInstances.item(j);
							appEndpoints.add(
									Integer.parseInt(anInstance.getElementsByTagName("port").item(0).getTextContent()));
						}
					}
				}
			}
			// at this point we have in servicexendpoints all services and endpoints in each
			// service available, now we have to check validity
			if (services.size() != this.services.size()) {
				return false;
			}
			Map<String, Integer> endpointxport = ServiceRegistryUtil.instance.getMappingEndpointxPort();
			for (String aService : services) {
				if (!this.services.contains(aService)) {
					return false;
				}
				for (String anEndpoint : this.endpoints.get(aService)) {
					if (!unavailable_endpoints.contains(anEndpoint)) {
						if (!servicexendpoints.get(aService).contains(endpointxport.get(anEndpoint))) {
							return false;
						}
					}
				}
			}
			return true;
		} catch (IOException | ParserConfigurationException | SAXException e) {
			throw new RuntimeException("exception when checkig for valid status", e);
		}
	}

	public void query_endpoints (String a_service) {
		// do nothing
	}

	public void query_health_apis(String an_endpoint) {
		// do nothing
	}

	public void register(String a_service) {
		if (services.contains(a_service)) {
			throw new RuntimeException("trying to register a already registered service: " + a_service);
		} else {
			this.services.add(a_service);
			this.endpoints.put(a_service, new SetString());
		}
	}

	public void add_endpoint(String a_health_check_api, String a_service, String an_endpoint) {
		if (services.contains(a_service)) {
			try {
				ServiceRegistryUtil.instance.registerEndpoint(a_service, an_endpoint);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
			if (!hcaxendpoint.containsKey(an_endpoint)) {
				hcaxendpoint.put(an_endpoint, new ArrayList<>());
			}
			hcaxendpoint.get(an_endpoint).add(a_health_check_api);
		} else {
			throw new RuntimeException("trying to add an endpoint to a non registered service: " + a_service);
		}
	}

	public void unregister(String a_service) {
		SetString serviceEndpoints = endpoints.get(a_service);
		for (String anEndpoint : serviceEndpoints) {
			remove_endpoint(a_service, anEndpoint);
		}
		this.endpoints.remove(a_service);
		this.services.remove(a_service);
	}

	public void health_check(HealthApiResponses health_api_responses) {
		this.unavailableEndpoints = new SetString();
		for (String aHelathApi : health_api_responses.keySet()) {
			if (!health_api_responses.get(aHelathApi))
				for (String anEndpoint : hcaxendpoint.get(aHelathApi)) {
					unavailableEndpoints.add(anEndpoint);
				}
		}
	}

	public void remove_endpoint(String a_service, String an_endpoint) {
		if (services.contains(a_service)) {
			try {
				ServiceRegistryUtil.instance.removeEndpoint(a_service, an_endpoint);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
			for (String hca : hcaxendpoint.keySet()) {
				hcaxendpoint.get(hca).remove(an_endpoint);
			}
		} else {
			throw new RuntimeException("trying to remove an endpoint to a non registered service: " + a_service);
		}
	}

}
