package com.mycompany.filmbuffauth.dao;

import java.util.Optional;

import com.mycompany.filmbuffauth.model.Users;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationUserRepository extends JpaRepository<Users, Integer>{
    
    public Optional<Users> findByEmail(String email);
}
