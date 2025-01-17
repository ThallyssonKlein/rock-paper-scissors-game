package com.rocketpaperscissors;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EntityScan(basePackages = {"com.rocketpaperscissors.ports.outbound.database"})
@EnableRetry
public class Application {

	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(Application.class);
		SpringApplication.run(Application.class, args);
	}

}
