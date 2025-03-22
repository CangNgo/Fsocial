package com.fsocial.relationshipService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude =  {DataSourceAutoConfiguration.class })
public class RelationshipServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(RelationshipServiceApplication.class, args);
	}

}
