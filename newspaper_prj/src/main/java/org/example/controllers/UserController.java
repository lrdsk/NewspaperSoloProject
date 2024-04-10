package org.example.controllers;

import org.example.dto.UserDTO;
import org.example.models.Post;
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
import java.util.Set;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserServiceImpl userService;

    @Autowired
    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping()
    public List<UserDTO> index(){
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public UserDTO getOne(@PathVariable("id") int userId){
        return userService.findById(userId);
    }

    /*@GetMapping("/{id}/like")
    public Set<Post> getLikes(@PathVariable("id") int userId){

    }*/

    @DeleteMapping("/{id}")
    public HttpEntity<HttpStatus> deleteUser(@PathVariable("id") int id){
        UserDTO userDTO = userService.findById(id);

        if(userDTO != null)
            userService.deleteById(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
