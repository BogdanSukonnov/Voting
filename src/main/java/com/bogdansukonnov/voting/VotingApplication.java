package com.bogdansukonnov.voting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class VotingApplication {

	public static void main(String[] args) {
		SpringApplication.run(VotingApplication.class, args);
	}

}
