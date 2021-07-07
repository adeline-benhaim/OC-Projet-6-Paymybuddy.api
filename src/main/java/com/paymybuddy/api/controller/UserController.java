package com.paymybuddy.api.controller;

import com.paymybuddy.api.model.User;
import com.paymybuddy.api.model.dto.UserDto;
import com.paymybuddy.api.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @GetMapping("/user")
    public Optional<User> getUserById() {
        return userService.getUser();
    }

    @PostMapping("/user")
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @PutMapping("/user")
    public UserDto updateUser(@RequestBody User user) {
        return userService.updateUser(user);
    }

    @DeleteMapping("/user")
    public void deleteUserByEmail(@RequestParam("password") String password) {
        userService.deleteCurrentUserByPassword(password);
    }


}
