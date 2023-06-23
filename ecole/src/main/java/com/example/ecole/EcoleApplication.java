package com.example.ecole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class EcoleApplication {
	private static final Logger logger = LoggerFactory.getLogger(EcoleApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(EcoleApplication.class, args);
		logger.info("information");




	}
}
