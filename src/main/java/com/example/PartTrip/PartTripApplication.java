package com.example.PartTrip;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.ZoneId;
import java.util.Map;
import java.util.TimeZone;

@SpringBootApplication
public class PartTripApplication {

	public static final ZoneId ZONE = ZoneId.of("Asia/Seoul");

	public static void main(String[] args) {
		TimeZone.setDefault(TimeZone.getTimeZone(ZONE));
		SpringApplication app = new SpringApplication(PartTripApplication.class);
		app.setDefaultProperties(Map.of("spring.jpa.open-in-view", "false"));
		app.run(args);
	}

}
