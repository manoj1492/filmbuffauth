package com.mycompany.filmbuffauth.controller;

import java.util.ArrayList;
import java.util.List;

import com.mycompany.filmbuffauth.model.AuthResponse;
import com.mycompany.filmbuffauth.model.LoginRequest;
import com.mycompany.filmbuffauth.model.Response;
import com.mycompany.filmbuffauth.service.LoginService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/auth")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @CrossOrigin("*")
    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<Response> login(@RequestBody LoginRequest loginRequest) {
        AuthResponse authResponse = loginService.login(loginRequest.getEmail(),loginRequest.getPassword());
        HttpHeaders headers = new HttpHeaders();
        List<String> headerlist = new ArrayList<>();
        List<String> exposeList = new ArrayList<>();
        headerlist.add("Content-Type");
        headerlist.add(" Accept");
        headerlist.add("X-Requested-With");
        headerlist.add("Authorization");
        headers.setAccessControlAllowHeaders(headerlist);
        exposeList.add("Authorization");
        headers.setAccessControlExposeHeaders(exposeList);
        headers.set("Authorization", authResponse.getIdToken());
        return ResponseEntity.status(HttpStatus.CREATED).headers(headers).body(new Response(authResponse));
    }
}
