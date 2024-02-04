package com.parkchoi.scrum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ScrumApplication {

	public static void main(String[] args) {
		SpringApplication.run(ScrumApplication.class, args);
	}

}
