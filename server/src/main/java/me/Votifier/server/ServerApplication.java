package me.Votifier.server;

import java.util.logging.Logger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ServerApplication {

	private static final Logger logger = Logger.getLogger(GracefulShutdown.class.getName());

	public static void main(String[] args) {
		logger.info("Starting server...");
		SpringApplication.run(ServerApplication.class, args);
	}
}
