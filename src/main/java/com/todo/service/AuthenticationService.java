package com.todo.service;

import com.todo.request_dto.AuthenticationRequest;
import com.todo.request_dto.RegisterRequest;
import com.todo.response_dto.AuthenticationResponse;

public interface AuthenticationService {
    void register(RegisterRequest input) throws Exception;
    AuthenticationResponse login(AuthenticationRequest loginRequest);
}
