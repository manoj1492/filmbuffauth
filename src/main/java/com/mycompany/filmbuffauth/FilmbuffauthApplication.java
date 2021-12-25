package com.mycompany.filmbuffauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class FilmbuffauthApplication {

	public static void main(String[] args) {
		SpringApplication.run(FilmbuffauthApplication.class, args);
	}

}
