package uy.edu.fing.svergara.serviceregistry.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class ServiceRegistryEurekaServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceRegistryEurekaServerApplication.class, args);
	}

}
