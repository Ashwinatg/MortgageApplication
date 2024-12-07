package com.hackathon.uob.MortgageApplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.hackathon.uob.repository")
@ComponentScan(basePackages = { "com.hackathon.uob.*" })
@EntityScan("com.hackathon.uob.*")
public class MortgageApplication {

	public static void main(String[] args) {
		SpringApplication.run(MortgageApplication.class, args);
	}

}
