package com.mycompany.filmbuffauth.model;

import java.util.Date;

public class AuthResponse {

    private String idToken;
    private Date expiresOn;

    public AuthResponse(String idToken, Date expiresOn) {
        this.idToken = idToken;
        this.expiresOn = expiresOn;
    }

    public String getIdToken() {
        return idToken;
    }

    public Date getExpiresOn() {
        return expiresOn;
    }

}
