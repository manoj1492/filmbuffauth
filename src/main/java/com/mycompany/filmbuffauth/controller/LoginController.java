package com.mycompany.filmbuffauth.controller;

import java.time.LocalDate;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.mycompany.filmbuffauth.config.JwtConfig;
import com.mycompany.filmbuffauth.model.AuthResponse;
import com.mycompany.filmbuffauth.model.LoginRequest;
import com.mycompany.filmbuffauth.model.Message;
import com.mycompany.filmbuffauth.service.ApplicationUserService;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/auth")
public class LoginController {

	private JwtConfig jwtConfig;
    private ApplicationUserService userService;
	private PasswordEncoder passwordEncoder;

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @PostMapping("/login")
    @ResponseBody
    public Mono <ResponseEntity<AuthResponse>> login(@RequestBody LoginRequest loginRequest) {
        logger.info("Start: login");
        Date expirationDate = java.sql.Date.valueOf(LocalDate.now().plusDays(jwtConfig.getExpirationTime()));
        return userService.findByUsername(loginRequest.getEmail())
                .filter(userDetails -> {
                		boolean isPasswordCorrect = passwordEncoder.matches(loginRequest.getPassword(), userDetails.getPassword());
                		return isPasswordCorrect;
                	})
                .map(userDetails -> ResponseEntity.ok(new AuthResponse(jwtConfig.generateToken(userDetails), expirationDate)))
                .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()));
    }

    @GetMapping("/dummy")
    public Mono<ResponseEntity<Message>> dummyUrl(){
    	return Mono.just(ResponseEntity.ok(new Message("hello")));

    }
}
