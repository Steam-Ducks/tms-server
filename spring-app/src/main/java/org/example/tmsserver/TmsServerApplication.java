package org.example.tmsserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TmsServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(TmsServerApplication.class, args);
		
	}

}
