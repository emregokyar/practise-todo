package com.todo.service;

import com.todo.request_dto.TodoRequest;
import com.todo.response_dto.TodoResponse;

public interface TodoService {
    TodoResponse createTodo(TodoRequest todoRequest);
}
