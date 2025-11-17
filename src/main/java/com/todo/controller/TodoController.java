package com.todo.controller;

import com.todo.request_dto.TodoRequest;
import com.todo.response_dto.TodoResponse;
import com.todo.service.TodoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get all todos for the user", description = "Fetch all todo from signed in user")
    public List<TodoResponse> getAllTodos() {
        return todoService.getAllTodos();
    }

    @Operation(summary = "Update todo for user", description = "Toggle the todo completion for the signed in user")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}")
    public TodoResponse toggleTodoCompletion(@PathVariable @Min(1) long id) {
        return todoService.toggleTodoCompletion(id);
    }

    @Operation(summary = "Delete todo for user", description = "Delete todo for singed in user")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteTodo(@PathVariable @Min(1) long id) {
        todoService.deleteTodo(id);
    }
}
