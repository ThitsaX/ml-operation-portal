package com.thitsaworks.dfsp_portal.api.hub_operator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class WebApiHubOperatorApplication {

	public static void main(String[] args) {

		SpringApplication.run(WebApiHubOperatorApplication.class, args);
	}

}
