package com.example.demo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.GenericContainer;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DemoApplicationTests {

	@Autowired
	TestRestTemplate restTemplate;

	private static int portDev = 8080;
	private static int portProd = 8081;


	public static GenericContainer<?> devapp = new GenericContainer<>("devapp").withExposedPorts(portDev);
	public static GenericContainer<?> prodapp = new GenericContainer<>("prodapp").withExposedPorts(portProd);

	@BeforeAll
	static void setUp(){
		devapp.start();
		prodapp.start();
	}

	@Test
	void contextLoadsDev() {
		ResponseEntity<String> forEntity = restTemplate.getForEntity(String.format("http://localhost:%s/profile",
				devapp.getMappedPort(portDev)), String.class);
		Assertions.assertEquals("Current profile is dev", forEntity.getBody());
	}

	@Test
	void contextLoadsProd(){
		ResponseEntity<String> forEntity = restTemplate.getForEntity(String.format("http://localhost:%s/profile",
				prodapp.getMappedPort(portProd)), String.class);
		Assertions.assertEquals("Current profile is production", forEntity.getBody());
	}
}
