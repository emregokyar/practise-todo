package com.todo.service;

import com.todo.response_dto.UserResponse;

public interface UserService {
    UserResponse getUserInfo();
    void deleteUser();
}
