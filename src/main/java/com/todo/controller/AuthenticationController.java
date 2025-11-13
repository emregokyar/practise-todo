package com.todo.controller;

import com.todo.request_dto.AuthenticationRequest;
import com.todo.request_dto.RegisterRequest;
import com.todo.response_dto.AuthenticationResponse;
import com.todo.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication REST API endpoints", description = "Operations related to login and register")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Operation(summary = "Register a new user", description = "Create a new user with an register body")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/register")
    public void register(@Valid @RequestBody RegisterRequest registerRequest) throws Exception {
        authenticationService.register(registerRequest);
    }

    @Operation(summary = "Login a user", description = "Login with user email and password")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/login")
    public AuthenticationResponse login(@Valid @RequestBody AuthenticationRequest authenticationRequest) {
        return authenticationService.login(authenticationRequest);
    }
}
