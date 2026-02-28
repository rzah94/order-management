package org.example.rzah.ordermanagement.controller;

import jakarta.validation.Valid;
import org.example.rzah.ordermanagement.dto.RequestCreatedUserDto;
import org.example.rzah.ordermanagement.dto.ResponseUserDto;
import org.example.rzah.ordermanagement.dto.UserPageDto;
import org.example.rzah.ordermanagement.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<UserPageDto> getUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        UserPageDto users = userService.getUsers(page, size);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseUserDto> getById(@PathVariable Long id) {
        ResponseUserDto user = userService.getUser(id);
        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<ResponseUserDto> createUser(@Valid @RequestBody RequestCreatedUserDto userDto) {
        ResponseUserDto user = userService.save(userDto);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }
}
