package com.todo.service;

import com.todo.request_dto.RegisterRequest;

public interface AuthenticationService {
    void register(RegisterRequest input) throws Exception;
}
