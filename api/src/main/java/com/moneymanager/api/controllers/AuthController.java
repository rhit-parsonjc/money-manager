package com.moneymanager.api.controllers;

import com.moneymanager.api.dtos.UserEntityDto;
import com.moneymanager.api.models.UserEntity;
import com.moneymanager.api.requests.UserUpdateRequest;
import com.moneymanager.api.services.MapperService.MapperService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.moneymanager.api.requests.LoginRequest;
import com.moneymanager.api.requests.RegisterRequest;
import com.moneymanager.api.responses.DataOrErrorResponse;
import com.moneymanager.api.security.JWTUtils;
import com.moneymanager.api.services.UserEntityService.UserEntityService;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final UserEntityService userEntityService;
    private final MapperService mapperService;
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

    @GetMapping("")
    public ResponseEntity<DataOrErrorResponse> getUser() {
        UserEntity userEntity = userEntityService.getAuthenticatedUserOrThrow();
        UserEntityDto userEntityDto = mapperService.mapUserEntityToDto(userEntity);
        DataOrErrorResponse response = new DataOrErrorResponse(true, userEntityDto);
        return new ResponseEntity<DataOrErrorResponse>(response, HttpStatus.OK);
    }

    @PatchMapping("")
    public ResponseEntity<DataOrErrorResponse> updateUser(@RequestBody UserUpdateRequest userUpdateRequest) {
        UserEntity userEntity = userEntityService.updateUser(userUpdateRequest);
        UserEntityDto userEntityDto = mapperService.mapUserEntityToDto(userEntity);
        DataOrErrorResponse response = new DataOrErrorResponse(true, userEntityDto);
        return new ResponseEntity<DataOrErrorResponse>(response, HttpStatus.OK);
    }

    @DeleteMapping("")
    public ResponseEntity<DataOrErrorResponse> deleteUser() {
        userEntityService.deleteUser();
        DataOrErrorResponse response = new DataOrErrorResponse(true, null);
        return new ResponseEntity<DataOrErrorResponse>(response, HttpStatus.NO_CONTENT);
    }
}
