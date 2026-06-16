package com.soso;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SosoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SosoApplication.class, args);
	}
}
