package com.mycompany.filmbuffauth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.mycompany.filmbuffauth.dao.ApplicationUserRepository;
import com.mycompany.filmbuffauth.model.ApplicationUser;
import com.mycompany.filmbuffauth.model.User;

import reactor.core.publisher.Mono;

@Service
public class ApplicationUserService implements ReactiveUserDetailsService {
	
	private ApplicationUserRepository applicationUserRepository;

    @Autowired
    public ApplicationUserService(ApplicationUserRepository applicationUserRepository) {
        this.applicationUserRepository = applicationUserRepository;
    }

	@Override
	public Mono<UserDetails> findByUsername(String username) {
		User user = applicationUserRepository.findByEmail(username)
	            .orElseThrow( () -> new UsernameNotFoundException(String.format("Username %s not found", username)));
	        
	        return Mono.just(new ApplicationUser(user));
	}

    
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//
//        User user = applicationUserRepository.findByEmail(username)
//            .orElseThrow( () -> new UsernameNotFoundException(String.format("Username %s not found", username)));
//        
//        return new ApplicationUser(user);
//    }

}
