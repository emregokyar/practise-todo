package com.todo.controller;

import com.todo.request_dto.TodoRequest;
import com.todo.response_dto.TodoResponse;
import com.todo.service.TodoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/todos")
@RestController
@Tag(name = "Todo REST API", description = "Operations for managing todos")
public class TodoController {
    private final TodoService todoService;

    @Autowired
    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create todo for the user", description = "Create todo for the signed in user")
    public TodoResponse createTodo(@Valid @RequestBody TodoRequest todoRequest) {

        return todoService.createTodo(todoRequest);
    }
}
