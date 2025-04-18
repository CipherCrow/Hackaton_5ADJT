package br.com.hackaton.priorizasus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class PriorizaSusApplication {

	public static void main(String[] args) {
		SpringApplication.run(PriorizaSusApplication.class, args);
	}

}
