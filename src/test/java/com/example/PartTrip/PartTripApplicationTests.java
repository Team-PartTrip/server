package com.example.PartTrip;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfEnvironmentVariable;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

@DisabledIfEnvironmentVariable(named = "CI", matches = "true")
@SpringBootTest(properties = "part-trip.scheduling.enabled=false")
@AutoConfigureTestDatabase
class PartTripApplicationTests {

	@Test
	void contextLoads() {
	}

}
