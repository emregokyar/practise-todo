package com.todo.service;

import com.todo.response_dto.UserResponse;

import java.util.List;

public interface AdminService {
    List<UserResponse> getAllUsers();

    UserResponse promoteToAdmin(long userId);

    void deleteNonAdminUser(long userId);
}
