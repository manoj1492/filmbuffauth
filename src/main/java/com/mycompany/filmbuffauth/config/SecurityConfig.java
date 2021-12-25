package com.mycompany.filmbuffauth.config;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfig{

	private AuthenticationManager authenticationManager;
    private SecurityContextRepository securityContextRepository;
    
    @Bean
	public SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http) throws Exception {
    	
    	http
        .exceptionHandling()
        .authenticationEntryPoint((swe, e) -> 
            Mono.fromRunnable(() -> swe.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED))
        ).accessDeniedHandler((swe, e) -> 
            Mono.fromRunnable(() -> swe.getResponse().setStatusCode(HttpStatus.FORBIDDEN))
        );
    	
    	http.cors();
    	
    	http.formLogin().disable();
        http.httpBasic().disable();
    	
        http.csrf().disable(); // To allow POST requests from POSTMAN. Disable CSRF (cross site request forgery)

        http.authenticationManager(authenticationManager);
        http.securityContextRepository(securityContextRepository);

        // Permit access to requests according to pattern and authorization.
        http.authorizeExchange()
                //.antMatchers("/**/dummy").permitAll()
                .pathMatchers("/auth/login").permitAll()
                .pathMatchers("/api/websocket/**").permitAll()
                .pathMatchers(HttpMethod.OPTIONS).permitAll()
                .pathMatchers("/auth/dummy").hasAuthority("QUIZ_READ")
                .pathMatchers("/api/v1/**").hasAuthority("QUIZ_READ");
        
        // Any other request should be authenticated. Order of these configurations is important.
        http.authorizeExchange().anyExchange().authenticated();
        
        return http.build();
    	
	}

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(Collections.singletonList("*"));
        config.setAllowedMethods(Collections.singletonList("*"));
        config.setAllowedHeaders(Collections.singletonList("*"));
        source.registerCorsConfiguration("/auth/**", config);
        source.registerCorsConfiguration("/api/v1/**", config);
        
        CorsConfiguration webSocketConfig = new CorsConfiguration();
        webSocketConfig.setAllowCredentials(true);
        webSocketConfig.setAllowedOriginPatterns(Collections.singletonList("*"));
        webSocketConfig.setAllowedMethods(Collections.singletonList("*"));
        webSocketConfig.setAllowedHeaders(Collections.singletonList("*"));
        source.registerCorsConfiguration("/api/websocket/**", webSocketConfig);

        return source;
    }

}
