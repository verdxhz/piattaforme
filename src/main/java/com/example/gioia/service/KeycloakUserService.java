package com.example.gioia.service;


import com.example.gioia.security.authentication.UserRegistrationRecord;

import org.springframework.http.ResponseEntity;

public interface KeycloakUserService {

    ResponseEntity createUser(UserRegistrationRecord userRegistrationRecord) throws Exception;

}
