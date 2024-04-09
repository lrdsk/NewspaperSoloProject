package org.example.controllers;

import org.example.dto.UserDTO;
import org.example.security.CustomUserDetails;
import org.example.servicesImpl.LikeServiceImpl;
import org.example.servicesImpl.UserServiceImpl;
import org.example.util.errorResponses.ErrorResponse;
import org.example.util.exceptions.PostNotFoundException;
import org.example.util.exceptions.UserNotFoundException;
import org.example.util.mappers.UserMapper;
import org.example.util.validators.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import sun.security.timestamp.HttpTimestamper;

import java.util.List;

@RestController
@RequestMapping("/api/users")
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

    @GetMapping()
    public List<UserDTO> index(){
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public UserDTO getOne(@PathVariable("id") int id){
        return userService.findById(id);
    }

    @DeleteMapping("/{id}")
    public HttpEntity<HttpStatus> deleteUser(@PathVariable("id") int id){
        UserDTO userDTO = userService.findById(id);

        if(userDTO != null)
            userService.deleteById(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
