package org.example.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.example.security.JWTUtil;
import org.example.services.servicesImpl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserServiceImpl userService;

    @Autowired
    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @Operation(summary = "Get all user likes by his email")
    @GetMapping("/likes")
    @ApiResponse(responseCode = "200", description = "List of id posts is liked")
    @ApiResponse(responseCode = "400", description = "Не отправлен header authorization Bearer token")
    @ApiResponse(responseCode = "403", description = "Valid jwt, but insufficient access rights")
    public Set<Integer> getLikes(@RequestHeader(name = "Authorization") String token){
        return userService.getSetPostLiked(token);
    }

}
