package com.aserto.example.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
public class GenericController {
    @DeleteMapping("/todos/{id}")
    @PreAuthorize("@aserto.check('group', 'admin', 'member')")
    public String deleteTodo(@PathVariable String id) {
        return "Hello from route DELETE /todos/{" + id + "}";
    }

    @GetMapping("/todos")
    @PreAuthorize("@aserto.check('group', 'viewer', 'member')")
    public String getTodo() {
        return "Hello from route GET /todos";
    }

    @GetMapping("/users/{userID}")
    @PreAuthorize("@aserto.check('group', 'viewer', 'member')")
    public String getUsers(@PathVariable String userID) {
        return "Hello from route GET /users/{" + userID + "}";
    }

    @PostMapping("/todos")
    @PreAuthorize("@aserto.check('group', 'editor', 'member')")
    public String postTodo() {
        return "Hello from route POST /todos";
    }

    @PutMapping("/todos/{id}")
    @PreAuthorize("@aserto.check('group', 'editor', 'member')")
    public String putTodo(@PathVariable String id) {
        return "Hello from route PUT /todos/{" + id + "}";
    }
}
