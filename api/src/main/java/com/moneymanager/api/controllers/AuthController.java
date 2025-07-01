package com.moneymanager.api.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moneymanager.api.requests.LoginRequest;
import com.moneymanager.api.requests.RegisterRequest;
import com.moneymanager.api.responses.DataOrErrorResponse;
import com.moneymanager.api.security.JWTUtils;
import com.moneymanager.api.services.UserEntityService.UserEntityService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final UserEntityService userEntityService;
    private final JWTUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    @PostMapping("login")
    public ResponseEntity<DataOrErrorResponse> login(@RequestBody LoginRequest loginRequest) {
        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateToken(authentication);
            DataOrErrorResponse response = new DataOrErrorResponse(true, jwt);
            return new ResponseEntity<DataOrErrorResponse>(response, HttpStatus.OK);
        } catch (AuthenticationException e) {
            DataOrErrorResponse response = new DataOrErrorResponse(false, e.getMessage());
            return new ResponseEntity<DataOrErrorResponse>(response, HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("register")
    public ResponseEntity<DataOrErrorResponse> register(@RequestBody RegisterRequest registerRequest) {
        userEntityService.createUser(registerRequest);
        DataOrErrorResponse response = new DataOrErrorResponse(true, null);
        return new ResponseEntity<DataOrErrorResponse>(response, HttpStatus.CREATED);
    }
}
