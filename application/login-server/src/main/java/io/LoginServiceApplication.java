package io;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;

@SpringBootApplication
public class LoginServiceApplication implements CommandLineRunner {

	private static Logger LOGGER = LoggerFactory.getLogger(LoginServiceApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(LoginServiceApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		LOGGER.info("initialized command line runner");
	}
}
