package org.example.controllers;

import org.example.dto.UserDTO;
import org.example.servicesImpl.UserServiceImpl;
import org.example.util.mappers.UserMapper;
import org.example.util.validators.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public UserDTO getOne(@PathVariable("id") int id){ //todo: реализовать выброс исключений и его последующую обработку
        UserDTO userDTO = null;
        if(userService.findById(id).isPresent())
            userDTO = userService.findById(id).get();

        return userDTO;
    }

    @DeleteMapping("/{id}")
    public HttpEntity<HttpStatus> deleteOne(@PathVariable("id") int id){ //todo: добавить проверку нашелся ли такой юзер по данному id
        userService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
