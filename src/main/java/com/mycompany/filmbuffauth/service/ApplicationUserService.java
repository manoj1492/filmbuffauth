package com.mycompany.filmbuffauth.service;

import com.mycompany.filmbuffauth.dao.ApplicationUserRepository;
import com.mycompany.filmbuffauth.model.ApplicationUser;
import com.mycompany.filmbuffauth.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ApplicationUserService implements UserDetailsService {

    private ApplicationUserRepository applicationUserRepository;

    @Autowired
    public ApplicationUserService(ApplicationUserRepository applicationUserRepository) {
        this.applicationUserRepository = applicationUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = applicationUserRepository.findByEmail(username)
            .orElseThrow( () -> new UsernameNotFoundException(String.format("Username %s not found", username)));
        
        return new ApplicationUser(user);
    }

}
