package org.example.controllers;

import org.example.dto.UserDTO;
import org.example.servicesImpl.UserServiceImpl;
import org.example.util.UserMapper;
import org.example.util.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {
    private final UserServiceImpl userService;
    private final UserMapper userMapper;
    private final UserValidator userValidator;

    @Autowired
    public UserController(UserServiceImpl userService, UserMapper userMapper, UserValidator userValidator) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.userValidator = userValidator;
    }

    @GetMapping("/users")
    public List<UserDTO> index(){
        return userService.findAll();
    }
}
