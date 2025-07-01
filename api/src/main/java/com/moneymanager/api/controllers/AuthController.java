package com.moneymanager.api.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moneymanager.api.requests.RegisterRequest;
import com.moneymanager.api.responses.DataOrErrorResponse;
import com.moneymanager.api.services.UserEntityService.UserEntityService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final UserEntityService userEntityService;

    @PostMapping("register")
    public ResponseEntity<DataOrErrorResponse> register(@RequestBody RegisterRequest registerRequest) {
        userEntityService.createUser(registerRequest);
        DataOrErrorResponse response = new DataOrErrorResponse(true, null);
        return new ResponseEntity<DataOrErrorResponse>(response, HttpStatus.CREATED);
    }
}
