package com.mycompany.filmbuffauth.service;

import java.time.LocalDate;
import java.util.Date;

import javax.crypto.SecretKey;

import com.mycompany.filmbuffauth.config.JwtConfig;
import com.mycompany.filmbuffauth.dao.ApplicationUserRepository;
import com.mycompany.filmbuffauth.model.AuthResponse;
import com.mycompany.filmbuffauth.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;

@Service
public class LoginService {

    private final PasswordEncoder passwordEncoder;
    private final JwtConfig jwtConfig;
    private final SecretKey secretKey;
    private final AuthenticationManager authenticationManager;
    private final ApplicationUserRepository userRepository;

    @Autowired
    public LoginService(PasswordEncoder passwordEncoder, JwtConfig jwtConfig, SecretKey secretKey,
    AuthenticationManager authenticationManager, ApplicationUserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.jwtConfig = jwtConfig;
        this.secretKey = secretKey;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
    }
    
    public AuthResponse login(String username, String password) {
            Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,
                    password));
            // Create token object
            // A JWT token consists of three parts - 1. Header 2. Body 3. Signature
            Date expirationDate = java.sql.Date.valueOf(LocalDate.now().plusDays(jwtConfig.getTokenExpirationAfterDays()));
            String token = Jwts.builder()
            .setSubject(authenticate.getName())
            .claim("authorities", authenticate.getAuthorities())
            .setIssuedAt(new Date())
            .setExpiration(expirationDate)
            .signWith(secretKey)
            .compact();
            
            AuthResponse response = new AuthResponse(token, expirationDate);
            return response;
    }

    public User saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()) );
        return userRepository.save(user);
    }

}
