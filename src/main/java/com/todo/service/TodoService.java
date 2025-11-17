package com.todo.service;

import com.todo.request_dto.TodoRequest;
import com.todo.response_dto.TodoResponse;

import java.util.List;

public interface TodoService {
    TodoResponse createTodo(TodoRequest todoRequest);

    List<TodoResponse> getAllTodos();

    TodoResponse toggleTodoCompletion(long id);

    void deleteTodo(long id);
}
