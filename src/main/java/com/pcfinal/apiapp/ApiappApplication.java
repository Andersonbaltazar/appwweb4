package com.pcfinal.apiapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan("com.pcfinal.apiapp.entity")
@EnableJpaRepositories("com.pcfinal.apiapp.repository")
public class ApiappApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiappApplication.class, args);
	}

}
