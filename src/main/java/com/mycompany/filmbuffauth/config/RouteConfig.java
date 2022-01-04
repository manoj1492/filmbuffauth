package com.mycompany.filmbuffauth.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteConfig {
	
	@Bean
	public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
		return builder.routes()
				.route("path_route", r -> 
					r.path("/api/v1/**")
					.filters(f -> f
							.rewritePath("/api", "")
							)
					.uri("lb://filmbuff-main"))
				.route("websocket_sockjs_route", r -> 
					r.path("/api/websocket/info/**")
					.filters(f -> f
							.rewritePath("/api", "")
							.setResponseHeader("Access-Control-Allow-Credentials", "true")
							.setResponseHeader("Access-Control-Allow-Origin", "https://filmbuff-client.herokuapp.com")
							//.removeResponseHeader("Access-Control-Allow-Origin")
							)
					.uri("lb://filmbuff-main"))
				.route("websocket_route", r -> 
					r.path("/api/websocket/**")
					.filters(f -> f
							.rewritePath("/api", "")
							.setResponseHeader("Access-Control-Allow-Credentials", "true")
							.setResponseHeader("Access-Control-Allow-Origin", "https://filmbuff-client.herokuapp.com")
							)
					.uri("lb://filmbuff-main"))
				.build();
	}

}
