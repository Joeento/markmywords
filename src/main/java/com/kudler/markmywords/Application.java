package com.kudler.markmywords;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main class that activates the application
 * and begins listening for requests sent to
 * the endpoints in our controllers.
 */
@SpringBootApplication
public class Application {
	/**
	 * Begin running server.
	 * @param args arguments for webserver to start
	 */
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
