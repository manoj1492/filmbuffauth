package com.mycompany.filmbuffauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@EnableZuulProxy  // act as zuul proxy.
@EnableEurekaServer //for making this application as eureka server.
@SpringBootApplication
public class FilmbuffauthApplication {

	public static void main(String[] args) {
		SpringApplication.run(FilmbuffauthApplication.class, args);
	}

}
