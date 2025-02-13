package com.fsocial.timelineservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class TimeLineServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TimeLineServiceApplication.class, args);
	}

}
