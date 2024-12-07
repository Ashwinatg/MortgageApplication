package com.hackathon.uob.MortgageApplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages  = {"com.hackathon.uob","com.hackathon.uob.repo","com.hackathon.uob.service","com.hackathon.uob.filter"})
@EnableJpaRepositories(basePackages  ={"com.hackathon.uob.repo"})
@EntityScan(basePackages = "com.hackathon.uob.entity")
public class MortgageApplication {

	public static void main(String[] args) {
		SpringApplication.run(MortgageApplication.class, args);
	}

}
