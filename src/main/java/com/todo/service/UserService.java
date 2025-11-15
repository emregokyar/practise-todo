package com.todo.service;

import com.todo.request_dto.PasswordUpdateRequest;
import com.todo.response_dto.UserResponse;

public interface UserService {
    UserResponse getUserInfo();
    void deleteUser();
    void updatePassword(PasswordUpdateRequest passwordUpdateRequest);
}
