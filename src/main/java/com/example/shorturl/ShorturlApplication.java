package com.example.shorturl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
public class ShorturlApplication {

	private static final Logger log = LoggerFactory.getLogger(ShorturlApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(ShorturlApplication.class, args);
	}

}
