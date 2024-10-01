package com.example.gioia.controllers;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.gioia.service.*;
import com.example.gioia.security.authentication.UserRegistrationRecord;
@RestController
@RequestMapping("/keycloak")
@AllArgsConstructor
class KeycloakController {
    @Autowired
    private final KeycloakUserService keycloakUserService;

    @PostMapping("/create")
    public ResponseEntity create(@RequestBody UserRegistrationRecord userRegistrationRecord) throws Exception {


        return keycloakUserService.createUser(userRegistrationRecord);

    }
}
