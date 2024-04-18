package org.example.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.example.dto.UserDTO;
import org.example.security.CustomUserDetails;
import org.example.servicesImpl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserServiceImpl userService;

    @Autowired
    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    /*@GetMapping()
    public List<UserDTO> index(){
        return userService.findAll();
    }*/
/*
    @GetMapping("/{id}")
    public UserDTO getOne(@PathVariable("id") int userId){
        return userService.findById(userId);
    }*/

    @Operation(summary = "Get all user likes by his id")
    @GetMapping("/likes")
    @ApiResponse(responseCode = "200", description = "List of id posts is liked")
    @ApiResponse(responseCode = "403", description = "Не отправлен header authorization Bearer token")
    @ApiResponse(responseCode = "500", description = "Invalid jwt-token to authorization")
    public Set<Integer> getLikes(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();
        return userService.getSetPostLiked(email);
    }

/*    @DeleteMapping("/{id}")
    public HttpEntity<HttpStatus> deleteUser(@PathVariable("id") int id){
        UserDTO userDTO = userService.findById(id);

        if(userDTO != null)
            userService.deleteById(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }*/

}
