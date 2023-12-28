package com.aserto.example.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
public class GenericController {
    @DeleteMapping("/todos/{id}")
//    @PreAuthorize("@check('group', new com.aserto.example.PathObjectIdMapper(#id), 'member')")
//    @PreAuthorize("@check('group', @objIdMapper, 'member')")
//    @PreAuthorize("@check('group', #id, 'member')")
    @PreAuthorize("@check.objectType('group').objectId(#id).relation('member').allowed()")
//    @PreAuthorize("@check.objectType('group').objectId('admin').relation('member').allowed()")
    public String deleteTodo(@PathVariable String id) {
        return "Hello from route DELETE /todos/{" + id + "}";
    }

    @GetMapping("/todos")
    @PreAuthorize("@check.objectType('group').objectId('viewer').relation('member').allowed()")
    public String getTodo() {
        return "Hello from route GET /todos";
    }

    @GetMapping("/users/{userID}")
    @PreAuthorize("@check.objectType('group').objectId('viewer').relation('member').allowed()")
    public String getUsers(@PathVariable String userID) {
        return "Hello from route GET /users/{" + userID + "}";
    }

    @PostMapping("/todos")
    @PreAuthorize("@check.objectType('group').objectId('editor').relation('member').allowed()")
    public String postTodo() {
        return "Hello from route POST /todos";
    }

    @PutMapping("/todos/{id}")
    @PreAuthorize("@check.objectType('group').objectId('editor').relation('member').allowed()")
    public String putTodo(@PathVariable String id) {
        return "Hello from route PUT /todos/{" + id + "}";
    }
}
