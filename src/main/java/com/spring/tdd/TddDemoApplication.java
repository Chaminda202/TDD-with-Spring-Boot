package com.spring.tdd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class TddDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(TddDemoApplication.class, args);
	}

}
