package com.mycompany.filmbuffauth.config;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class AuthenticationManager implements ReactiveAuthenticationManager{
	
	private JwtConfig jwtUtil;

    @Override
    @SuppressWarnings("unchecked")
    public Mono<Authentication> authenticate(Authentication authentication) {
        String authToken = authentication.getCredentials().toString();
        String username = jwtUtil.getUsernameFromToken(authToken);
        return Mono.just(jwtUtil.validateToken(authToken))
            .filter(valid -> valid)
            .switchIfEmpty(Mono.empty())
            .map(valid -> {
                Claims claims = jwtUtil.getAllClaimsFromToken(authToken);
                List<Map<String, String>> rolesMap = claims.get("authorities", List.class);
                
                return new UsernamePasswordAuthenticationToken(
                    username,
                    null,
                    rolesMap.stream().map(authority -> 
                     new SimpleGrantedAuthority(
                    		 authority.get("authority")
                        )).collect(Collectors.toList())
                );
            });
    }

}
