package com.nexaro.ecomapplication.controller;

import com.nexaro.ecomapplication.dto.UserRequest;
import com.nexaro.ecomapplication.dto.UserResponse;
import com.nexaro.ecomapplication.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/api/users")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return new ResponseEntity<>(userService.fetchAllUsers(), HttpStatus.OK);
    }

    @GetMapping("/api/users/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long id) {
        return userService.fetchUser(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/api/users")
    public ResponseEntity<String> createUsers(@RequestBody UserRequest UserRequest) {
        userService.addUser(UserRequest);
        return new ResponseEntity<>("User Added Sucessfully", HttpStatus.OK);
    }

    @PutMapping("/api/users/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Long id,
                                             @RequestBody UserRequest updateUserRequest) {
        boolean updated = userService.updateUser(id, updateUserRequest);

        if (updated) {
            return ResponseEntity.ok("User updated successfully");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("User not found");
    }
}
