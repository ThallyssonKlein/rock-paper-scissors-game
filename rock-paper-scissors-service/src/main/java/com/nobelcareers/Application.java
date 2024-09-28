package com.nobelcareers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = {"com.nobelcareers.ports.outbound.database"})
public class Application {

	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(Application.class);
		SpringApplication.run(Application.class, args);
	}

}
