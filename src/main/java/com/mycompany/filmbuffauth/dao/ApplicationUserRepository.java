package com.mycompany.filmbuffauth.dao;

import java.util.Optional;

import com.mycompany.filmbuffauth.model.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationUserRepository extends JpaRepository<User, Integer>{
    
    public Optional<User> findByEmail(String email);
}
