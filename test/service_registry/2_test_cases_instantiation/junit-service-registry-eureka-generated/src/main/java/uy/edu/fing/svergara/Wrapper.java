package uy.edu.fing.svergara;

public class Wrapper {
	
	public void initialisation (Endpoints endpoints, HealthApis health_apis, SetString last_endpoint_active_query_result, SetString last_health_query_result, SetString services, SetString unavailable_endpoints) {
		throw new RuntimeException("code not implemented yet");
	}
		
	public Boolean isValid (Endpoints endpoints, HealthApis health_apis, SetString last_endpoint_active_query_result, SetString last_health_query_result, SetString services, SetString unavailable_endpoints) {
		throw new RuntimeException("code not implemented yet");
	}
		
	public void query_endpoints (String a_service) {
		throw new RuntimeException("code not implemented yet");
	}
		
	public void query_health_apis (String an_endpoint) {
		throw new RuntimeException("code not implemented yet");
	}
		
	public void register (String a_service) {
		throw new RuntimeException("code not implemented yet");
	}
		
	public void add_endpoint (String a_health_check_api, String a_service, String an_endpoint) {
		throw new RuntimeException("code not implemented yet");
	}
		
	public void unregister (String a_service) {
		throw new RuntimeException("code not implemented yet");
	}
		
	public void health_check (HealthApiResponses health_api_responses) {
		throw new RuntimeException("code not implemented yet");
	}
		
	public void remove_endpoint (String a_service, String an_endpoint) {
		throw new RuntimeException("code not implemented yet");
	}
		
}
