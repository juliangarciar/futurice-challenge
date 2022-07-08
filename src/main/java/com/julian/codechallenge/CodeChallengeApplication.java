package com.julian.codechallenge;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CodeChallengeApplication {

	private static final Logger LOGGER = LoggerFactory.getLogger(CodeChallengeApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(CodeChallengeApplication.class, args);

		LOGGER.info("Hello World");
	}

}
