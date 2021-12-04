package com.bh.intra.plp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class PlpApplication {

	public static void main(String[] args) {
		SpringApplication.run(PlpApplication.class, args);
	}

}
